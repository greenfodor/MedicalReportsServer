package com.greenfodor.medical_reports_server.plugins

import com.greenfodor.medical_reports_server.db.DatabaseFactory
import com.greenfodor.medical_reports_server.db.dao.*
import com.greenfodor.medical_reports_server.model.UserRoles
import com.greenfodor.medical_reports_server.model.ErrorResponse
import com.typesafe.config.ConfigFactory
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.mindrot.jbcrypt.BCrypt

val appConfig = HoconApplicationConfig(ConfigFactory.load("application.conf"))

fun Application.configureRouting() {
    val simpleJwt = SimpleJWT()

    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(e.localizedMessage, HttpStatusCode.InternalServerError.value)
            )
        }
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(javaClass.classLoader, "templates")
    }
    install(Authentication) {
        jwt {
            verifier(simpleJwt.buildJwtVerifier())
            validate { UserIdPrincipal(it.payload.getClaim("id").asString()) }
        }
    }

    DatabaseFactory.init()
    val userService = UserService()
    val patientService = PatientService()

    routing {
        post("/users/create") {
            val newUser = call.receive<NewUser>()

            val existingUser = userService.getUserByEmail(newUser.email)
            if (existingUser != null) {
                throw Throwable("Email address already in use")
            }

            userService.createUser(newUser)
            call.respond(HttpStatusCode.OK)
        }

        post("/login") {
            val post = call.receive<LoginUser>()

            val user = userService.getUserByEmail(post.email)
            if (user == null || !BCrypt.checkpw(post.password, user.password) || user.role == null) {
                throw Throwable("Invalid Credentials")
            }

            val token = simpleJwt.sign(user.id)
            call.respond(HttpStatusCode.OK, user.toLoginResponse(token))
        }

        authenticate {
            get("/users/me") {
                val principal = call.principal<UserIdPrincipal>() ?: throw Throwable("No principal decoded")
                val userId = principal.name
                val user = userService.getUserById(userId) ?: throw Throwable("User not found")

                call.respond(user)
            }

            get("/dashboard") {
                val users = userService.getAllUsers()
                call.respond(FreeMarkerContent("dashboard.ftl", mapOf("users" to users.sortedBy { it.id })))
            }

            post("/dashboard") {
                val principal = call.principal<UserIdPrincipal>() ?: throw Throwable("No principal decoded")
                val userId = principal.name
                val user = userService.getUserById(userId) ?: throw Throwable("User not found")

                if (user.role != UserRoles.ADMIN.value) throw Throwable("You do not have the right privileges")

                val parameters = call.receiveParameters()
                val userIdToUpdate = parameters["userId"] ?: throw Throwable("userId is required")

                when (val role = parameters["role"]) {
                    UserRoles.MEDICAL_LABORATORY_PROFESSIONAL.value,
                    UserRoles.PHYSICIAN.value,
                    UserRoles.NURSE.value -> userService.assignRole(userIdToUpdate, role)
                }

                val users = userService.getAllUsers()
                call.respond(FreeMarkerContent("dashboard.ftl", mapOf("users" to users.sortedBy { it.id })))
            }

            post("/patients/register") {
                val principal = call.principal<UserIdPrincipal>() ?: throw Throwable("No principal decoded")

                val userId = principal.name
                val user = userService.getUserById(userId) ?: throw Throwable("User not found")
                if (user.role != UserRoles.NURSE.value) throw Throwable("You do not have the right privileges")

                val newPatient = call.receive<NewPatient>()
                val patient = patientService.createPatient(newPatient) ?: throw Throwable("Patient could not be saved")

                call.respond(HttpStatusCode.OK, RegisterPatientResponse(patient.id))
            }

            get("/patients/{patientId}") {
                val principal = call.principal<UserIdPrincipal>() ?: throw Throwable("No principal decoded")

                val userId = principal.name
                userService.getUserById(userId) ?: throw Throwable("User not found")

                val patientId = call.parameters["patientId"] ?: throw Throwable("Patient ID is required")
                val patient = patientService.getPatientById(patientId) ?: throw Throwable("Patient does not exist")

                call.respond(HttpStatusCode.OK, patient.toGetPatientResponse())
            }
        }
    }
}
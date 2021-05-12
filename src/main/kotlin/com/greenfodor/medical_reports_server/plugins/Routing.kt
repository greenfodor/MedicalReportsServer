package com.greenfodor.medical_reports_server.plugins

import com.greenfodor.medical_reports_server.db.DatabaseFactory
import com.greenfodor.medical_reports_server.db.dao.LoginUser
import com.greenfodor.medical_reports_server.db.dao.NewUser
import com.greenfodor.medical_reports_server.db.dao.UserService
import com.greenfodor.medical_reports_server.db.model.UserRoles
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
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
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

    routing {
        post("/users/create") {
            val newUser = call.receive<NewUser>()

            val existingUser = userService.getUserByEmail(newUser.email)
            if (existingUser != null) {
                throw Throwable("email address already in use")
            }

            userService.createUser(newUser)
            call.respond("wait for your account to receive a role")
        }

        post("/login") {
            val post = call.receive<LoginUser>()

            val user = userService.getUserByEmail(post.email)
            if (user == null || !BCrypt.checkpw(post.password, user.password) || user.role == null) {
                throw Throwable("Invalid Credentials")
            }

            val token = simpleJwt.sign(user.id)
            call.respondText("token: $token")
        }

        authenticate {
            get("/users/me") {
                val principal = call.principal<UserIdPrincipal>() ?: throw Throwable("No principal decoded")
                val userId = principal.name
                val user = userService.getUserById(userId) ?: throw Throwable("user not found")

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
        }
    }
}
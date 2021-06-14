package com.greenfodor.medical_reports_server.db

import com.greenfodor.medical_reports_server.db.dao.MedicalReports
import com.greenfodor.medical_reports_server.db.dao.Patients
import com.greenfodor.medical_reports_server.db.dao.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Patients)
            SchemaUtils.create(MedicalReports)

//            Patients.insert {
//                it[name] = "George"
//                it[dob] = DateTime.parse("13-03-1998", DateTimeFormat.forPattern("dd-MM-yyyy"))
//                it[gender] = "male"
//            }
            //Create a default user
//            Users.insert {
//                it[name] = "george"
//                it[email] = "test@unu.com"
//                it[password] = BCrypt.hashpw("password", BCrypt.gensalt())
//            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig("/hikari.properties")
        return HikariDataSource(config)
    }
}
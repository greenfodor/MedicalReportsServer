package com.greenfodor.medical_reports_server.db

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
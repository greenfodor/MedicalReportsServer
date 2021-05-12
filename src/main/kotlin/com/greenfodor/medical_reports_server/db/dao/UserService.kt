package com.greenfodor.medical_reports_server.db.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserService {
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { it.toUser() }
    }

    fun getUserByEmail(email: String): AuthUser? = transaction {
        Users.select { Users.email eq email }
            .mapNotNull { it.toAuthUser() }
            .singleOrNull()
    }

    fun getUserById(id: String): User? = transaction {
        Users.select { Users.id eq UUID.fromString(id) }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun createUser(newUser: NewUser) = transaction {
        Users.insert {
            it[name] = newUser.name
            it[email] = newUser.email
            it[password] = BCrypt.hashpw(newUser.password, BCrypt.gensalt())
            it[role] = null
        }
    }

    fun assignRole(userId: String, role: String) = transaction {
        Users.update({ Users.id eq UUID.fromString(userId) }) {
            it[Users.role] = role
        }
    }

    private fun ResultRow.toUser(): User = User(
        id = this[Users.id].toString(),
        name = this[Users.name],
        email = this[Users.email],
        role = this[Users.role]
    )

    private fun ResultRow.toAuthUser(): AuthUser = AuthUser(
        id = this[Users.id].toString(),
        name = this[Users.name].toString(),
        email = this[Users.email],
        password = this[Users.password],
        role = this[Users.role]
    )
}
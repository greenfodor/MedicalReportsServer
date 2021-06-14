package com.greenfodor.medical_reports_server.db.dao

import com.google.gson.annotations.SerializedName
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Users : UUIDTable("users") {
    val name: Column<String> = varchar("name", 100)
    val email: Column<String> = varchar("email", 100)
    val password: Column<String> = varchar("password", 100)
    val role: Column<String?> = varchar("role", 20).nullable()
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String?
)

data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String?
) {
    fun toLoginResponse(token: String) = LoginResponse(User(id, name, email, role), token)
}

data class NewUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginUser(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("user")
    val user: User,
    @SerializedName("token")
    val token: String
)
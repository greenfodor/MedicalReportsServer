package com.greenfodor.medical_reports_server.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

class SimpleJWT {
    private val algorithm = Algorithm.HMAC256(appConfig.property("ktor.jwt.secret").getString())

    fun buildJwtVerifier(): JWTVerifier = JWT.require(algorithm).build()

    fun sign(id: String): String = JWT.create().withClaim("id", id).sign(algorithm)
}
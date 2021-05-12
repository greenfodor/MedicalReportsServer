package com.greenfodor.medical_reports_server

import com.greenfodor.medical_reports_server.plugins.configureRouting
import io.ktor.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
}

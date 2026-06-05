package de

import io.ktor.server.application.Application

fun Application.module() {
    configureSerialization()
    configureRouting()
}
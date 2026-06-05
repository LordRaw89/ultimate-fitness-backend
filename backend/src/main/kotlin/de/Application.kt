package de

import de.config.DatabaseConfig
import io.ktor.server.application.Application

fun Application.module() {
    DatabaseConfig.validateEnvironmentVariables()
    configureSerialization()
    configureRouting()
}
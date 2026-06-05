package de.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseConfig {

    fun init() {
        val databaseUrl = requireEnvironmentVariable("DATABASE_URL")
        val databaseUser = requireEnvironmentVariable("DATABASE_USER")
        val databasePassword = requireEnvironmentVariable("DATABASE_PASSWORD")

        val dataSource = createDataSource(
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword
        )

        Database.connect(dataSource)
        testConnection()

        println("Database connection pool was initialized.")
    }

    private fun createDataSource(
        databaseUrl: String,
        databaseUser: String,
        databasePassword: String
    ): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = databaseUrl
            username = databaseUser
            password = databasePassword

            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
            poolName = "UltimateFitnessPool"
        }

        return HikariDataSource(hikariConfig)
    }

    private fun requireEnvironmentVariable(name: String): String {
        return System.getenv(name)
            ?: error("$name environment variable is not set.")
    }

    private fun testConnection() {
        transaction {
            exec("SELECT 1")
        }

        println("Database connection test was successful.")
    }
}
package de.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseConfig {

    private var dataSource: HikariDataSource? = null

    private var currentConfig: DatabaseConnectionConfig? = null

    private data class DatabaseConnectionConfig(
        val databaseUrl: String,
        val databaseUser: String,
        val databasePassword: String
    )

    fun init() {
        init(
            databaseUrl = requireEnvironmentVariable("DATABASE_URL"),
            databaseUser = requireEnvironmentVariable("DATABASE_USER"),
            databasePassword = requireEnvironmentVariable("DATABASE_PASSWORD")
        )
    }

    @Synchronized
    fun init(
        databaseUrl: String,
        databaseUser: String,
        databasePassword: String
    ) {
        val requestedConfig = DatabaseConnectionConfig(
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword
        )

        val existingDataSource = dataSource

        if (
            currentConfig == requestedConfig &&
            existingDataSource != null &&
            !existingDataSource.isClosed
        ) {
            return
        }

        existingDataSource?.close()

        val newDataSource = createDataSource(
            databaseUrl = databaseUrl,
            databaseUser = databaseUser,
            databasePassword = databasePassword
        )

        try {
            Database.connect(newDataSource)

            dataSource = newDataSource
            currentConfig = requestedConfig

            testConnection()

            println("Database connection pool was initialized.")
        } catch (exception: Exception) {
            newDataSource.close()

            dataSource = null
            currentConfig = null

            throw exception
        }
    }

    @Synchronized
    fun shutdown() {
        dataSource?.close()

        dataSource = null
        currentConfig = null
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
            minimumIdle = 1

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

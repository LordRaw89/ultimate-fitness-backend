package de.config

import kotlin.test.assertEquals
import org.junit.Test
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class DatabaseConnectionTest {

    @Test
    fun `database connection executes select one successfully`() {
        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val result = transaction {
            exec("SELECT 1") { resultSet ->
                if (resultSet.next()) {
                    resultSet.getInt(1)
                } else {
                    null
                }
            }
        }

        assertEquals(1, result)
    }
}

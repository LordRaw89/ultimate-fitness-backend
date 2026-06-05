package de.config

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class DatabaseConnectionTest {

    @Test
    fun `database connection executes select one successfully`() {
        DatabaseConfig.init()

        transaction {
            exec("SELECT 1") { resultSet ->
                assertTrue(resultSet.next())
                assertEquals(1, resultSet.getInt(1))
            }
        }
    }
}
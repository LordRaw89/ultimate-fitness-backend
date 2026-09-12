package de

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.*
import de.config.DatabaseConfig

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        application {
            configureSerialization()
            configureRouting()
        }

        assertEquals(
            HttpStatusCode.OK,
            client.get("/").status
        )
    }
}

package de.testsupport

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

object SupabaseAuthTestHelper {

    private val supabaseUrl =
        System.getenv("TEST_SUPABASE_URL")
            ?: error("TEST_SUPABASE_URL is not set.")

    private val secretKey =
        System.getenv("TEST_SUPABASE_SECRET_KEY")
            ?: error("TEST_SUPABASE_SECRET_KEY is not set.")

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun createTestUser(
        email: String,
        password: String
    ): UUID {
        val response = client.post("$supabaseUrl/auth/v1/admin/users") {
            header("apikey", secretKey)
            header(HttpHeaders.Authorization, "Bearer $secretKey")
            header(HttpHeaders.ContentType, ContentType.Application.Json)

            setBody(
                CreateUserRequest(
                    email = email,
                    password = password,
                    emailConfirm = true
                )
            )
        }

        check(response.status.isSuccess()) {
            "Could not create test user. HTTP status: ${response.status}"
        }

        return UUID.fromString(response.body<CreateUserResponse>().id)
    }

    suspend fun deleteTestUser(userId: UUID) {
        val response = client.delete(
            "$supabaseUrl/auth/v1/admin/users/$userId"
        ) {
            header("apikey", secretKey)
            header(HttpHeaders.Authorization, "Bearer $secretKey")
        }

        check(response.status.isSuccess()) {
            "Could not delete test user. HTTP status: ${response.status}"
        }
    }
}

@Serializable
private data class CreateUserRequest(
    val email: String,
    val password: String,
    @SerialName("email_confirm")
    val emailConfirm: Boolean
)

@Serializable
private data class CreateUserResponse(
    val id: String
)
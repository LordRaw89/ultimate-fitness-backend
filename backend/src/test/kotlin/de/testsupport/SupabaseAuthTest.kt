package de.testsupport

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull

class SupabaseAuthTest {

    @Test
    fun `creates and deletes local test user`(): Unit = runBlocking {
        val email = "test-${System.currentTimeMillis()}@example.com"
        val password = "TestPassword123!"

        val userId = SupabaseAuthTestHelper.createTestUser(
            email = email,
            password = password
        )

        try {
            assertNotNull(userId)
        } finally {
            SupabaseAuthTestHelper.deleteTestUser(userId)
        }
    }
}

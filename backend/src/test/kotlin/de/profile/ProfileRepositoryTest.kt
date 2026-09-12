package de.profile

import de.config.DatabaseConfig
import de.testsupport.SupabaseAuthTestHelper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProfileRepositoryTest {

    @Test
    fun `creates and reads profile for auth user`() = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val email = "profile-test-${System.currentTimeMillis()}@example.com"
        val password = "TestPassword123!"
        val username = "testuser-${System.currentTimeMillis()}"

        val userId = SupabaseAuthTestHelper.createTestUser(
            email = email,
            password = password
        )

        try {
            ProfileRepository.createProfile(
                profileId = userId,
                username = username
            )

            val profile = ProfileRepository.findById(userId)

            assertNotNull(profile)
            assertEquals(userId, profile.profileId)
            assertEquals(username, profile.username)

        } finally {
            ProfileRepository.deleteProfile(userId)
            SupabaseAuthTestHelper.deleteTestUser(userId)
        }
    }
}

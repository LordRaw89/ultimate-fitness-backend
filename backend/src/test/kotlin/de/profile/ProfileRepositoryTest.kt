package de.profile

import de.config.DatabaseConfig
import de.testsupport.SupabaseAuthTestHelper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertFails

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

    @Test
    fun `rejects duplicate username`(): Unit = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val password = "TestPassword123!"
        val username = "duplicate-user-${System.currentTimeMillis()}"

        val firstUserId = SupabaseAuthTestHelper.createTestUser(
            email = "first-${System.currentTimeMillis()}@example.com",
            password = password
        )

        val secondUserId = SupabaseAuthTestHelper.createTestUser(
            email = "second-${System.currentTimeMillis()}@example.com",
            password = password
        )

        try {
            ProfileRepository.createProfile(
                profileId = firstUserId,
                username = username
            )

            assertFails {
                ProfileRepository.createProfile(
                    profileId = secondUserId,
                    username = username
                )
            }

        } finally {
            ProfileRepository.deleteProfile(firstUserId)
            ProfileRepository.deleteProfile(secondUserId)

            SupabaseAuthTestHelper.deleteTestUser(firstUserId)
            SupabaseAuthTestHelper.deleteTestUser(secondUserId)
        }
    }

    @Test
    fun `updates username`() = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val userId = SupabaseAuthTestHelper.createTestUser(
            email = "update-${System.currentTimeMillis()}@example.com",
            password = "TestPassword123!"
        )

        val originalUsername = "before-${System.currentTimeMillis()}"
        val updatedUsername = "after-${System.currentTimeMillis()}"

        try {
            ProfileRepository.createProfile(
                profileId = userId,
                username = originalUsername
            )

            ProfileRepository.updateUsername(
                profileId = userId,
                username = updatedUsername
            )

            val profile = ProfileRepository.findById(userId)

            assertNotNull(profile)
            assertEquals(updatedUsername, profile.username)

        } finally {
            ProfileRepository.deleteProfile(userId)
            SupabaseAuthTestHelper.deleteTestUser(userId)
        }
    }
}

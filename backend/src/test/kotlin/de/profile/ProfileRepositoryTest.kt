package de.profile

import de.config.DatabaseConfig
import de.testsupport.SupabaseAuthTestHelper
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertFails
import kotlin.test.assertTrue
import kotlin.test.assertNull

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

            val profileBeforeUpdate = ProfileRepository.findById(userId)

            assertNotNull(profileBeforeUpdate)

            ProfileRepository.updateUsername(
                profileId = userId,
                username = updatedUsername
            )

            val profileAfterUpdate = ProfileRepository.findById(userId)

            assertNotNull(profileAfterUpdate)
            assertEquals(updatedUsername, profileAfterUpdate.username)

            assertTrue(
                profileAfterUpdate.updatedAt.isAfter(profileBeforeUpdate.updatedAt)
            )

        } finally {
            ProfileRepository.deleteProfile(userId)
            SupabaseAuthTestHelper.deleteTestUser(userId)
        }
    }

    @Test
    fun `deletes profile`() = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val userId = SupabaseAuthTestHelper.createTestUser(
            email = "delete-${System.currentTimeMillis()}@example.com",
            password = "TestPassword123!"
        )

        val username = "delete-user-${System.currentTimeMillis()}"

        try {
            ProfileRepository.createProfile(
                profileId = userId,
                username = username
            )

            val profileBeforeDelete = ProfileRepository.findById(userId)
            assertNotNull(profileBeforeDelete)

            ProfileRepository.deleteProfile(userId)

            val profileAfterDelete = ProfileRepository.findById(userId)
            assertNull(profileAfterDelete)

        } finally {
            SupabaseAuthTestHelper.deleteTestUser(userId)
        }
    }

    @Test
    fun `returns null for unknown profile id`() = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val unknownProfileId = UUID.randomUUID()

        val profile = ProfileRepository.findById(unknownProfileId)

        assertNull(profile)
    }

    @Test
    fun `returns zero when updating unknown profile`() = runBlocking {

        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )

        val unknownProfileId = UUID.randomUUID()

        val updatedRows = ProfileRepository.updateUsername(
            profileId = unknownProfileId,
            username = "does-not-exist"
        )

        assertEquals(0, updatedRows)
    }
}

package de.trainingday

import de.config.DatabaseConfig
import de.profile.ProfileRepository
import de.testsupport.SupabaseAuthTestHelper
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TrainingDayRepositoryTest {

    private fun initializeTestDatabase() {
        DatabaseConfig.init(
            databaseUrl = System.getenv("TEST_DATABASE_URL")
                ?: error("TEST_DATABASE_URL is not set."),
            databaseUser = System.getenv("TEST_DATABASE_USER")
                ?: error("TEST_DATABASE_USER is not set."),
            databasePassword = System.getenv("TEST_DATABASE_PASSWORD")
                ?: error("TEST_DATABASE_PASSWORD is not set.")
        )
    }

    private suspend fun createTestUserWithProfile(prefix: String): UUID {
        val suffix = UUID.randomUUID().toString().take(8)

        val userId = SupabaseAuthTestHelper.createTestUser(
            email = "$prefix-$suffix@example.com",
            password = "TestPassword123!"
        )

        ProfileRepository.createProfile(
            profileId = userId,
            username = "$prefix-$suffix"
        )

        return userId
    }

    private suspend fun deleteTestUserWithProfile(userId: UUID) {
        ProfileRepository.deleteProfile(userId)
        SupabaseAuthTestHelper.deleteTestUser(userId)
    }

    @Test
    fun `creates and reads training day`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("training-day")
        var trainingDayId: Long? = null

        val trainingDate = LocalDate(2026, 9, 14)
        val notes = "Strength training"

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = trainingDate,
                notes = notes
            )

            assertTrue(trainingDayId > 0)

            val trainingDay = TrainingDayRepository.findById(trainingDayId)

            assertNotNull(trainingDay)
            assertEquals(trainingDayId, trainingDay.trainingDayId)
            assertEquals(userId, trainingDay.profileId)
            assertEquals(trainingDate, trainingDay.trainingDate)
            assertEquals(notes, trainingDay.notes)

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `updates notes and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("update-notes")
        var trainingDayId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 15),
                notes = "Original notes"
            )

            val beforeUpdate =
                TrainingDayRepository.findById(trainingDayId)

            assertNotNull(beforeUpdate)

            val updatedRows = TrainingDayRepository.updateNotes(
                trainingDayId = trainingDayId,
                notes = "Updated notes"
            )

            assertEquals(1, updatedRows)

            val afterUpdate =
                TrainingDayRepository.findById(trainingDayId)

            assertNotNull(afterUpdate)
            assertEquals("Updated notes", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows clearing notes`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("clear-notes")
        var trainingDayId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 16),
                notes = "Notes to remove"
            )

            val updatedRows = TrainingDayRepository.updateNotes(
                trainingDayId = trainingDayId,
                notes = null
            )

            assertEquals(1, updatedRows)

            val trainingDay =
                TrainingDayRepository.findById(trainingDayId)

            assertNotNull(trainingDay)
            assertNull(trainingDay.notes)

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `deletes training day`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("delete-day")

        val trainingDayId = TrainingDayRepository.createTrainingDay(
            profileId = userId,
            trainingDate = LocalDate(2026, 9, 17),
            notes = null
        )

        try {
            val beforeDelete =
                TrainingDayRepository.findById(trainingDayId)

            assertNotNull(beforeDelete)

            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            val afterDelete =
                TrainingDayRepository.findById(trainingDayId)

            assertNull(afterDelete)

        } finally {
            // Safe even if the test already deleted the row.
            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `returns null for unknown training day id`() = runBlocking {
        initializeTestDatabase()

        val trainingDay =
            TrainingDayRepository.findById(Long.MAX_VALUE)

        assertNull(trainingDay)
    }

    @Test
    fun `returns zero when updating unknown training day`() = runBlocking {
        initializeTestDatabase()

        val updatedRows = TrainingDayRepository.updateNotes(
            trainingDayId = Long.MAX_VALUE,
            notes = "Does not exist"
        )

        assertEquals(0, updatedRows)
    }

    @Test
    fun `rejects duplicate training date for same profile`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("duplicate-date")
        var firstTrainingDayId: Long? = null

        val trainingDate = LocalDate(2026, 9, 18)

        try {
            firstTrainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = trainingDate,
                    notes = "First"
                )

            assertFails {
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = trainingDate,
                    notes = "Duplicate"
                )
            }

        } finally {
            firstTrainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows same training date for different profiles`(): Unit = runBlocking {
        initializeTestDatabase()

        val firstUserId =
            createTestUserWithProfile("same-date-first")

        val secondUserId =
            createTestUserWithProfile("same-date-second")

        var firstTrainingDayId: Long? = null
        var secondTrainingDayId: Long? = null

        val trainingDate = LocalDate(2026, 9, 19)

        try {
            firstTrainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = firstUserId,
                    trainingDate = trainingDate
                )

            secondTrainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = secondUserId,
                    trainingDate = trainingDate
                )

            assertNotNull(
                TrainingDayRepository.findById(firstTrainingDayId)
            )

            assertNotNull(
                TrainingDayRepository.findById(secondTrainingDayId)
            )

        } finally {
            firstTrainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            secondTrainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(firstUserId)
            deleteTestUserWithProfile(secondUserId)
        }
    }

    @Test
    fun `rejects training day for missing profile`(): Unit = runBlocking {
        initializeTestDatabase()

        val unknownProfileId = UUID.randomUUID()

        assertFails {
            TrainingDayRepository.createTrainingDay(
                profileId = unknownProfileId,
                trainingDate = LocalDate(2026, 9, 20),
                notes = null
            )
        }
    }
}

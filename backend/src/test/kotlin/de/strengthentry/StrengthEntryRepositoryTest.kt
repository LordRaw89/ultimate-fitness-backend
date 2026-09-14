package de.strengthentry

import de.config.DatabaseConfig
import de.exercise.ExercisesTable
import de.profile.ProfileRepository
import de.testsupport.SupabaseAuthTestHelper
import de.trainingday.TrainingDayRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StrengthEntryRepositoryTest {

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

    private suspend fun findSeedExerciseId(
        exerciseName: String = "Ring Push Ups"
    ): Long {
        return suspendTransaction {
            ExercisesTable
                .selectAll()
                .where {
                    ExercisesTable.exerciseName eq exerciseName
                }
                .singleOrNull()
                ?.get(ExercisesTable.exerciseId)
                ?: error(
                    "Seed exercise '$exerciseName' was not found. " +
                            "Make sure the local Supabase seed data has been applied."
                )
        }
    }

    @Test
    fun `creates and reads strength entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-entry")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 21)
            )

            strengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    notes = "Strength test entry"
                )

            assertTrue(strengthEntryId > 0)

            val strengthEntry =
                StrengthEntryRepository.findById(strengthEntryId)

            assertNotNull(strengthEntry)
            assertEquals(strengthEntryId, strengthEntry.strengthEntryId)
            assertEquals(trainingDayId, strengthEntry.trainingDayId)
            assertEquals(exerciseId, strengthEntry.exerciseId)
            assertEquals("Strength test entry", strengthEntry.notes)

        } finally {
            strengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `updates notes and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-update")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 22)
            )

            strengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    notes = "Original notes"
                )

            val beforeUpdate =
                StrengthEntryRepository.findById(strengthEntryId)

            assertNotNull(beforeUpdate)

            val updatedRows =
                StrengthEntryRepository.updateNotes(
                    strengthEntryId = strengthEntryId,
                    notes = "Updated notes"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                StrengthEntryRepository.findById(strengthEntryId)

            assertNotNull(afterUpdate)
            assertEquals("Updated notes", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            strengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows clearing notes`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-clear")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 23)
            )

            strengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    notes = "Notes to remove"
                )

            val updatedRows =
                StrengthEntryRepository.updateNotes(
                    strengthEntryId = strengthEntryId,
                    notes = null
                )

            assertEquals(1, updatedRows)

            val strengthEntry =
                StrengthEntryRepository.findById(strengthEntryId)

            assertNotNull(strengthEntry)
            assertNull(strengthEntry.notes)

        } finally {
            strengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `deletes strength entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-delete")
        val exerciseId = findSeedExerciseId()

        val trainingDayId =
            TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 9, 24)
            )

        val strengthEntryId =
            StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

        try {
            assertNotNull(
                StrengthEntryRepository.findById(strengthEntryId)
            )

            StrengthEntryRepository.deleteStrengthEntry(strengthEntryId)

            assertNull(
                StrengthEntryRepository.findById(strengthEntryId)
            )

        } finally {
            // Safe if the row was already deleted above.
            StrengthEntryRepository.deleteStrengthEntry(strengthEntryId)
            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `returns null for unknown strength entry id`() = runBlocking {
        initializeTestDatabase()

        val strengthEntry =
            StrengthEntryRepository.findById(Long.MAX_VALUE)

        assertNull(strengthEntry)
    }

    @Test
    fun `returns zero when updating unknown strength entry`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            StrengthEntryRepository.updateNotes(
                strengthEntryId = Long.MAX_VALUE,
                notes = "Does not exist"
            )

        assertEquals(0, updatedRows)
    }

    @Test
    fun `rejects duplicate exercise on same training day`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-duplicate")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var firstStrengthEntryId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 9, 25)
                )

            firstStrengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId
                )

            assertFails {
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId
                )
            }

        } finally {
            firstStrengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows same exercise on different training days`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("strength-multi-day")
        val exerciseId = findSeedExerciseId()

        var firstTrainingDayId: Long? = null
        var secondTrainingDayId: Long? = null

        var firstStrengthEntryId: Long? = null
        var secondStrengthEntryId: Long? = null

        try {
            firstTrainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 9, 26)
                )

            secondTrainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 9, 27)
                )

            firstStrengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = firstTrainingDayId,
                    exerciseId = exerciseId
                )

            secondStrengthEntryId =
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = secondTrainingDayId,
                    exerciseId = exerciseId
                )

            assertNotNull(
                StrengthEntryRepository.findById(firstStrengthEntryId)
            )

            assertNotNull(
                StrengthEntryRepository.findById(secondStrengthEntryId)
            )

        } finally {
            firstStrengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            secondStrengthEntryId?.let {
                StrengthEntryRepository.deleteStrengthEntry(it)
            }

            firstTrainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            secondTrainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `rejects strength entry for missing training day`(): Unit = runBlocking {
        initializeTestDatabase()

        val exerciseId = findSeedExerciseId()

        assertFails {
            StrengthEntryRepository.createStrengthEntry(
                trainingDayId = Long.MAX_VALUE,
                exerciseId = exerciseId
            )
        }
    }

    @Test
    fun `rejects strength entry for missing exercise`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("strength-missing-exercise")

        var trainingDayId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 9, 28)
                )

            assertFails {
                StrengthEntryRepository.createStrengthEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = Long.MAX_VALUE
                )
            }

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }
}

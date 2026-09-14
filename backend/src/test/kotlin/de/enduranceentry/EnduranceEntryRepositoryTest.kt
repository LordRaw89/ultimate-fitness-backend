package de.enduranceentry

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

class EnduranceEntryRepositoryTest {

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
        exerciseName: String = "Running"
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
                            "Run supabase db reset first."
                )
        }
    }

    @Test
    fun `creates and reads endurance entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-entry")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var enduranceEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 20)
            )

            enduranceEntryId =
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 1800,
                    notes = "30 minute run"
                )

            assertTrue(enduranceEntryId > 0)

            val entry =
                EnduranceEntryRepository.findById(enduranceEntryId)

            assertNotNull(entry)
            assertEquals(enduranceEntryId, entry.enduranceEntryId)
            assertEquals(trainingDayId, entry.trainingDayId)
            assertEquals(exerciseId, entry.exerciseId)
            assertEquals(1800, entry.durationSec)
            assertEquals("30 minute run", entry.notes)

        } finally {
            enduranceEntryId?.let {
                EnduranceEntryRepository.deleteEnduranceEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `updates endurance entry and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-update")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var enduranceEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 21)
            )

            enduranceEntryId =
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 1200,
                    notes = "Before"
                )

            val beforeUpdate =
                EnduranceEntryRepository.findById(enduranceEntryId)

            assertNotNull(beforeUpdate)

            val updatedRows =
                EnduranceEntryRepository.updateEnduranceEntry(
                    enduranceEntryId = enduranceEntryId,
                    durationSec = 2400,
                    notes = "After"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                EnduranceEntryRepository.findById(enduranceEntryId)

            assertNotNull(afterUpdate)
            assertEquals(2400, afterUpdate.durationSec)
            assertEquals("After", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            enduranceEntryId?.let {
                EnduranceEntryRepository.deleteEnduranceEntry(it)
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

        val userId = createTestUserWithProfile("endurance-clear")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var enduranceEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 22)
            )

            enduranceEntryId =
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 1800,
                    notes = "Remove me"
                )

            val updatedRows =
                EnduranceEntryRepository.updateEnduranceEntry(
                    enduranceEntryId = enduranceEntryId,
                    durationSec = 1800,
                    notes = null
                )

            assertEquals(1, updatedRows)

            val entry =
                EnduranceEntryRepository.findById(enduranceEntryId)

            assertNotNull(entry)
            assertNull(entry.notes)

        } finally {
            enduranceEntryId?.let {
                EnduranceEntryRepository.deleteEnduranceEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `deletes endurance entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-delete")
        val exerciseId = findSeedExerciseId()

        val trainingDayId =
            TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 23)
            )

        val enduranceEntryId =
            EnduranceEntryRepository.createEnduranceEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId,
                durationSec = 1800
            )

        try {
            assertNotNull(
                EnduranceEntryRepository.findById(enduranceEntryId)
            )

            EnduranceEntryRepository.deleteEnduranceEntry(enduranceEntryId)

            assertNull(
                EnduranceEntryRepository.findById(enduranceEntryId)
            )

        } finally {
            EnduranceEntryRepository.deleteEnduranceEntry(enduranceEntryId)
            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `rejects zero duration`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-zero")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 24)
            )

            assertFails {
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 0
                )
            }

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `rejects endurance entry for missing training day`(): Unit = runBlocking {
        initializeTestDatabase()

        val exerciseId = findSeedExerciseId()

        assertFails {
            EnduranceEntryRepository.createEnduranceEntry(
                trainingDayId = Long.MAX_VALUE,
                exerciseId = exerciseId,
                durationSec = 1800
            )
        }
    }

    @Test
    fun `rejects endurance entry for missing exercise`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-missing-exercise")

        var trainingDayId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 25)
            )

            assertFails {
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = Long.MAX_VALUE,
                    durationSec = 1800
                )
            }

        } finally {
            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows same exercise multiple times on same training day`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("endurance-multiple")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var firstEntryId: Long? = null
        var secondEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 26)
            )

            firstEntryId =
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 1800
                )

            secondEntryId =
                EnduranceEntryRepository.createEnduranceEntry(
                    trainingDayId = trainingDayId,
                    exerciseId = exerciseId,
                    durationSec = 900
                )

            assertNotNull(
                EnduranceEntryRepository.findById(firstEntryId)
            )

            assertNotNull(
                EnduranceEntryRepository.findById(secondEntryId)
            )

        } finally {
            firstEntryId?.let {
                EnduranceEntryRepository.deleteEnduranceEntry(it)
            }

            secondEntryId?.let {
                EnduranceEntryRepository.deleteEnduranceEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `returns null for unknown endurance entry id`() = runBlocking {
        initializeTestDatabase()

        val entry =
            EnduranceEntryRepository.findById(Long.MAX_VALUE)

        assertNull(entry)
    }

    @Test
    fun `returns zero when updating unknown endurance entry`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            EnduranceEntryRepository.updateEnduranceEntry(
                enduranceEntryId = Long.MAX_VALUE,
                durationSec = 1800,
                notes = null
            )

        assertEquals(0, updatedRows)
    }
}

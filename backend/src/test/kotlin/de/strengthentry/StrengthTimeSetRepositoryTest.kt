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

class StrengthTimeSetRepositoryTest {

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
        exerciseName: String = "Ring L-Sit"
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
    fun `creates and reads strength time set`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-set")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var timeSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 10)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            timeSetId = StrengthTimeSetRepository.createStrengthTimeSet(
                strengthEntryId = strengthEntryId,
                setNumber = 1,
                durationSec = 45,
                notes = "First hold"
            )

            assertTrue(timeSetId > 0)

            val timeSet = StrengthTimeSetRepository.findById(timeSetId)

            assertNotNull(timeSet)
            assertEquals(timeSetId, timeSet.strengthTimeSetId)
            assertEquals(strengthEntryId, timeSet.strengthEntryId)
            assertEquals(1.toShort(), timeSet.setNumber)
            assertEquals(45, timeSet.durationSec)
            assertEquals("First hold", timeSet.notes)

        } finally {
            timeSetId?.let {
                StrengthTimeSetRepository.deleteStrengthTimeSet(it)
            }

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
    fun `updates strength time set and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-update")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var timeSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 11)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            timeSetId = StrengthTimeSetRepository.createStrengthTimeSet(
                strengthEntryId = strengthEntryId,
                setNumber = 1,
                durationSec = 30,
                notes = "Before"
            )

            val beforeUpdate =
                StrengthTimeSetRepository.findById(timeSetId)

            assertNotNull(beforeUpdate)

            val updatedRows =
                StrengthTimeSetRepository.updateStrengthTimeSet(
                    strengthTimeSetId = timeSetId,
                    setNumber = 1,
                    durationSec = 60,
                    notes = "After"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                StrengthTimeSetRepository.findById(timeSetId)

            assertNotNull(afterUpdate)
            assertEquals(60, afterUpdate.durationSec)
            assertEquals("After", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            timeSetId?.let {
                StrengthTimeSetRepository.deleteStrengthTimeSet(it)
            }

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

        val userId = createTestUserWithProfile("time-clear")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var timeSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 12)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            timeSetId = StrengthTimeSetRepository.createStrengthTimeSet(
                strengthEntryId = strengthEntryId,
                setNumber = 1,
                durationSec = 30,
                notes = "Remove me"
            )

            val updatedRows =
                StrengthTimeSetRepository.updateStrengthTimeSet(
                    strengthTimeSetId = timeSetId,
                    setNumber = 1,
                    durationSec = 30,
                    notes = null
                )

            assertEquals(1, updatedRows)

            val timeSet =
                StrengthTimeSetRepository.findById(timeSetId)

            assertNotNull(timeSet)
            assertNull(timeSet.notes)

        } finally {
            timeSetId?.let {
                StrengthTimeSetRepository.deleteStrengthTimeSet(it)
            }

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
    fun `deletes strength time set`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-delete")
        val exerciseId = findSeedExerciseId()

        val trainingDayId = TrainingDayRepository.createTrainingDay(
            profileId = userId,
            trainingDate = LocalDate(2026, 10, 13)
        )

        val strengthEntryId = StrengthEntryRepository.createStrengthEntry(
            trainingDayId = trainingDayId,
            exerciseId = exerciseId
        )

        val timeSetId = StrengthTimeSetRepository.createStrengthTimeSet(
            strengthEntryId = strengthEntryId,
            setNumber = 1,
            durationSec = 30
        )

        try {
            assertNotNull(
                StrengthTimeSetRepository.findById(timeSetId)
            )

            StrengthTimeSetRepository.deleteStrengthTimeSet(timeSetId)

            assertNull(
                StrengthTimeSetRepository.findById(timeSetId)
            )

        } finally {
            StrengthTimeSetRepository.deleteStrengthTimeSet(timeSetId)
            StrengthEntryRepository.deleteStrengthEntry(strengthEntryId)
            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `rejects duplicate set number for same strength entry`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-duplicate")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var firstTimeSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 14)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            firstTimeSetId =
                StrengthTimeSetRepository.createStrengthTimeSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    durationSec = 30
                )

            assertFails {
                StrengthTimeSetRepository.createStrengthTimeSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    durationSec = 45
                )
            }

        } finally {
            firstTimeSetId?.let {
                StrengthTimeSetRepository.deleteStrengthTimeSet(it)
            }

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
    fun `rejects zero set number`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-zero-set")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 15)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            assertFails {
                StrengthTimeSetRepository.createStrengthTimeSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 0,
                    durationSec = 30
                )
            }

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
    fun `rejects zero duration`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("time-zero-duration")
        val exerciseId = findSeedExerciseId()

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 16)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            assertFails {
                StrengthTimeSetRepository.createStrengthTimeSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    durationSec = 0
                )
            }

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
    fun `rejects time set for missing strength entry`(): Unit = runBlocking {
        initializeTestDatabase()

        assertFails {
            StrengthTimeSetRepository.createStrengthTimeSet(
                strengthEntryId = Long.MAX_VALUE,
                setNumber = 1,
                durationSec = 30
            )
        }
    }

    @Test
    fun `returns null for unknown time set id`() = runBlocking {
        initializeTestDatabase()

        val timeSet =
            StrengthTimeSetRepository.findById(Long.MAX_VALUE)

        assertNull(timeSet)
    }

    @Test
    fun `returns zero when updating unknown time set`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            StrengthTimeSetRepository.updateStrengthTimeSet(
                strengthTimeSetId = Long.MAX_VALUE,
                setNumber = 1,
                durationSec = 30,
                notes = null
            )

        assertEquals(0, updatedRows)
    }
}

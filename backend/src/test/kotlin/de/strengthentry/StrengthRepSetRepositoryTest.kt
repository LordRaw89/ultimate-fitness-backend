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
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StrengthRepSetRepositoryTest {

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

    private suspend fun findSeedExerciseId(exerciseName: String): Long {
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
    fun `creates and reads strength rep set`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-set")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var repSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 1)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            repSetId = StrengthRepSetRepository.createStrengthRepSet(
                strengthEntryId = strengthEntryId,
                setNumber = 1,
                reps = 12,
                weightKg = BigDecimal("25.50"),
                notes = "First set"
            )

            assertTrue(repSetId > 0)

            val repSet = StrengthRepSetRepository.findById(repSetId)

            assertNotNull(repSet)
            assertEquals(repSetId, repSet.strengthRepSetId)
            assertEquals(strengthEntryId, repSet.strengthEntryId)
            assertEquals(1.toShort(), repSet.setNumber)
            assertEquals(12.toShort(), repSet.reps)
            assertEquals(BigDecimal("25.50"), repSet.weightKg)
            assertEquals("First set", repSet.notes)

        } finally {
            repSetId?.let {
                StrengthRepSetRepository.deleteStrengthRepSet(it)
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
    fun `updates strength rep set and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-update")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var repSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 2)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            repSetId = StrengthRepSetRepository.createStrengthRepSet(
                strengthEntryId = strengthEntryId,
                setNumber = 1,
                reps = 10,
                weightKg = BigDecimal("20.00"),
                notes = "Before"
            )

            val beforeUpdate =
                StrengthRepSetRepository.findById(repSetId)

            assertNotNull(beforeUpdate)

            val updatedRows =
                StrengthRepSetRepository.updateStrengthRepSet(
                    strengthRepSetId = repSetId,
                    setNumber = 1,
                    reps = 8,
                    weightKg = BigDecimal("30.00"),
                    notes = "After"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                StrengthRepSetRepository.findById(repSetId)

            assertNotNull(afterUpdate)
            assertEquals(8.toShort(), afterUpdate.reps)
            assertEquals(BigDecimal("30.00"), afterUpdate.weightKg)
            assertEquals("After", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            repSetId?.let {
                StrengthRepSetRepository.deleteStrengthRepSet(it)
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
    fun `rejects duplicate set number for same strength entry`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-duplicate")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var firstRepSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 3)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            firstRepSetId =
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    reps = 10,
                    weightKg = BigDecimal("20.00")
                )

            assertFails {
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    reps = 8,
                    weightKg = BigDecimal("25.00")
                )
            }

        } finally {
            firstRepSetId?.let {
                StrengthRepSetRepository.deleteStrengthRepSet(it)
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

        val userId = createTestUserWithProfile("rep-zero-set")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 4)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            assertFails {
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 0,
                    reps = 10,
                    weightKg = BigDecimal("20.00")
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
    fun `rejects zero reps`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-zero-reps")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 5)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            assertFails {
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    reps = 0,
                    weightKg = BigDecimal("20.00")
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
    fun `rejects negative weight`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-negative-weight")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 6)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            assertFails {
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    reps = 10,
                    weightKg = BigDecimal("-0.01")
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
    fun `allows zero weight`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-zero-weight")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        var trainingDayId: Long? = null
        var strengthEntryId: Long? = null
        var repSetId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 7)
            )

            strengthEntryId = StrengthEntryRepository.createStrengthEntry(
                trainingDayId = trainingDayId,
                exerciseId = exerciseId
            )

            repSetId =
                StrengthRepSetRepository.createStrengthRepSet(
                    strengthEntryId = strengthEntryId,
                    setNumber = 1,
                    reps = 10,
                    weightKg = BigDecimal.ZERO
                )

            assertNotNull(
                StrengthRepSetRepository.findById(repSetId)
            )

        } finally {
            repSetId?.let {
                StrengthRepSetRepository.deleteStrengthRepSet(it)
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
    fun `rejects rep set for missing strength entry`(): Unit = runBlocking {
        initializeTestDatabase()

        assertFails {
            StrengthRepSetRepository.createStrengthRepSet(
                strengthEntryId = Long.MAX_VALUE,
                setNumber = 1,
                reps = 10,
                weightKg = BigDecimal("20.00")
            )
        }
    }

    @Test
    fun `returns null for unknown rep set id`() = runBlocking {
        initializeTestDatabase()

        val repSet =
            StrengthRepSetRepository.findById(Long.MAX_VALUE)

        assertNull(repSet)
    }

    @Test
    fun `returns zero when updating unknown rep set`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            StrengthRepSetRepository.updateStrengthRepSet(
                strengthRepSetId = Long.MAX_VALUE,
                setNumber = 1,
                reps = 10,
                weightKg = BigDecimal("20.00"),
                notes = null
            )

        assertEquals(0, updatedRows)
    }

    @Test
    fun `deletes strength rep set`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("rep-delete")
        val exerciseId = findSeedExerciseId("Ring Push Ups")

        val trainingDayId = TrainingDayRepository.createTrainingDay(
            profileId = userId,
            trainingDate = LocalDate(2026, 10, 8)
        )

        val strengthEntryId = StrengthEntryRepository.createStrengthEntry(
            trainingDayId = trainingDayId,
            exerciseId = exerciseId
        )

        val repSetId = StrengthRepSetRepository.createStrengthRepSet(
            strengthEntryId = strengthEntryId,
            setNumber = 1,
            reps = 10,
            weightKg = BigDecimal("20.00")
        )

        try {
            assertNotNull(
                StrengthRepSetRepository.findById(repSetId)
            )

            StrengthRepSetRepository.deleteStrengthRepSet(repSetId)

            assertNull(
                StrengthRepSetRepository.findById(repSetId)
            )

        } finally {
            StrengthRepSetRepository.deleteStrengthRepSet(repSetId)
            StrengthEntryRepository.deleteStrengthEntry(strengthEntryId)
            TrainingDayRepository.deleteTrainingDay(trainingDayId)

            deleteTestUserWithProfile(userId)
        }
    }
}

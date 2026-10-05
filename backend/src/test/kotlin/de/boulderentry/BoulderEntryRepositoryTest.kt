package de.boulderentry

import de.config.DatabaseConfig
import de.profile.ProfileRepository
import de.testsupport.SupabaseAuthTestHelper
import de.trainingday.TrainingDayRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BoulderEntryRepositoryTest {

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
    fun `creates and reads boulder entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("boulder-entry")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 27)
            )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId,
                    notes = "Evening bouldering session"
                )

            assertTrue(boulderEntryId > 0)

            val entry =
                BoulderEntryRepository.findById(boulderEntryId)

            assertNotNull(entry)
            assertEquals(boulderEntryId, entry.boulderEntryId)
            assertEquals(trainingDayId, entry.trainingDayId)
            assertEquals(
                "Evening bouldering session",
                entry.notes
            )

        } finally {
            boulderEntryId?.let {
                BoulderEntryRepository.deleteBoulderEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `updates boulder entry notes and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("boulder-update")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 28)
            )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId,
                    notes = "Before"
                )

            val beforeUpdate =
                BoulderEntryRepository.findById(boulderEntryId)

            assertNotNull(beforeUpdate)

            val updatedRows =
                BoulderEntryRepository.updateNotes(
                    boulderEntryId = boulderEntryId,
                    notes = "After"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                BoulderEntryRepository.findById(boulderEntryId)

            assertNotNull(afterUpdate)
            assertEquals("After", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt.isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            boulderEntryId?.let {
                BoulderEntryRepository.deleteBoulderEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `allows clearing boulder entry notes`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("boulder-clear")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 29)
            )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId,
                    notes = "Remove me"
                )

            val updatedRows =
                BoulderEntryRepository.updateNotes(
                    boulderEntryId = boulderEntryId,
                    notes = null
                )

            assertEquals(1, updatedRows)

            val entry =
                BoulderEntryRepository.findById(boulderEntryId)

            assertNotNull(entry)
            assertNull(entry.notes)

        } finally {
            boulderEntryId?.let {
                BoulderEntryRepository.deleteBoulderEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `deletes boulder entry`() = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("boulder-delete")

        val trainingDayId =
            TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 30)
            )

        val boulderEntryId =
            BoulderEntryRepository.createBoulderEntry(
                trainingDayId = trainingDayId
            )

        try {
            assertNotNull(
                BoulderEntryRepository.findById(boulderEntryId)
            )

            BoulderEntryRepository.deleteBoulderEntry(
                boulderEntryId
            )

            assertNull(
                BoulderEntryRepository.findById(boulderEntryId)
            )

        } finally {
            BoulderEntryRepository.deleteBoulderEntry(
                boulderEntryId
            )

            TrainingDayRepository.deleteTrainingDay(
                trainingDayId
            )

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `rejects boulder entry for missing training day`(): Unit = runBlocking {
        initializeTestDatabase()

        assertFails {
            BoulderEntryRepository.createBoulderEntry(
                trainingDayId = Long.MAX_VALUE
            )
        }
    }

    @Test
    fun `allows multiple boulder entries on same training day`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId = createTestUserWithProfile("boulder-multiple")

        var trainingDayId: Long? = null
        var firstEntryId: Long? = null
        var secondEntryId: Long? = null

        try {
            trainingDayId = TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 10, 31)
            )

            firstEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId,
                    notes = "First session"
                )

            secondEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId,
                    notes = "Second session"
                )

            assertNotNull(
                BoulderEntryRepository.findById(firstEntryId)
            )

            assertNotNull(
                BoulderEntryRepository.findById(secondEntryId)
            )

        } finally {
            firstEntryId?.let {
                BoulderEntryRepository.deleteBoulderEntry(it)
            }

            secondEntryId?.let {
                BoulderEntryRepository.deleteBoulderEntry(it)
            }

            trainingDayId?.let {
                TrainingDayRepository.deleteTrainingDay(it)
            }

            deleteTestUserWithProfile(userId)
        }
    }

    @Test
    fun `returns null for unknown boulder entry id`() = runBlocking {
        initializeTestDatabase()

        val entry =
            BoulderEntryRepository.findById(Long.MAX_VALUE)

        assertNull(entry)
    }

    @Test
    fun `returns zero when updating unknown boulder entry`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            BoulderEntryRepository.updateNotes(
                boulderEntryId = Long.MAX_VALUE,
                notes = "Nothing"
            )

        assertEquals(0, updatedRows)
    }
}

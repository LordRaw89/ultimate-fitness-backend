package de.boulderentry

import de.config.DatabaseConfig
import de.difficultygrade.DifficultyGradesTable
import de.profile.ProfileRepository
import de.testsupport.SupabaseAuthTestHelper
import de.trainingday.TrainingDayRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BoulderRouteRepositoryTest {

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

    private suspend fun createTestUserWithProfile(
        prefix: String
    ): UUID {
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

    private suspend fun deleteTestUserWithProfile(
        userId: UUID
    ) {
        ProfileRepository.deleteProfile(userId)
        SupabaseAuthTestHelper.deleteTestUser(userId)
    }

    private suspend fun findAnyDifficultyGradeId(): Long {
        return suspendTransaction {
            DifficultyGradesTable
                .selectAll()
                .limit(1)
                .singleOrNull()
                ?.get(DifficultyGradesTable.difficultyGradeId)
                ?: error(
                    "No difficulty grade found. " +
                            "Make sure the local Supabase seed data has been applied."
                )
        }
    }

    @Test
    fun `creates and reads boulder route`() = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-route")

        val difficultyGradeId =
            findAnyDifficultyGradeId()

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null
        var boulderRouteId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 1)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            boulderRouteId =
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    difficultyGradeId = difficultyGradeId,
                    routeName = "Blue Route",
                    attempts = 3,
                    result = "top",
                    notes = "Good route"
                )

            assertTrue(boulderRouteId > 0)

            val route =
                BoulderRouteRepository.findById(
                    boulderRouteId
                )

            assertNotNull(route)

            assertEquals(
                boulderRouteId,
                route.boulderRouteId
            )

            assertEquals(
                boulderEntryId,
                route.boulderEntryId
            )

            assertEquals(
                difficultyGradeId,
                route.difficultyGradeId
            )

            assertEquals(
                "Blue Route",
                route.routeName
            )

            assertEquals(
                3,
                route.attempts.toInt()
            )

            assertEquals(
                "top",
                route.result
            )

            assertEquals(
                "Good route",
                route.notes
            )

        } finally {
            boulderRouteId?.let {
                BoulderRouteRepository.deleteBoulderRoute(it)
            }

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
    fun `updates boulder route and updated timestamp`() = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-route-update")

        val difficultyGradeId =
            findAnyDifficultyGradeId()

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null
        var boulderRouteId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 2)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            boulderRouteId =
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    difficultyGradeId = difficultyGradeId,
                    routeName = "Before",
                    attempts = 4,
                    result = "zone",
                    notes = "Before"
                )

            val beforeUpdate =
                BoulderRouteRepository.findById(
                    boulderRouteId
                )

            assertNotNull(beforeUpdate)

            val updatedRows =
                BoulderRouteRepository.updateBoulderRoute(
                    boulderRouteId = boulderRouteId,
                    difficultyGradeId = difficultyGradeId,
                    routeName = "After",
                    attempts = 2,
                    result = "top",
                    notes = "After"
                )

            assertEquals(1, updatedRows)

            val afterUpdate =
                BoulderRouteRepository.findById(
                    boulderRouteId
                )

            assertNotNull(afterUpdate)

            assertEquals("After", afterUpdate.routeName)
            assertEquals(2, afterUpdate.attempts.toInt())
            assertEquals("top", afterUpdate.result)
            assertEquals("After", afterUpdate.notes)

            assertTrue(
                afterUpdate.updatedAt
                    .isAfter(beforeUpdate.updatedAt)
            )

        } finally {
            boulderRouteId?.let {
                BoulderRouteRepository.deleteBoulderRoute(it)
            }

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
    fun `allows clearing nullable route fields`() = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-route-clear")

        val difficultyGradeId =
            findAnyDifficultyGradeId()

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null
        var boulderRouteId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 3)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            boulderRouteId =
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    difficultyGradeId = difficultyGradeId,
                    routeName = "Named Route",
                    attempts = 2,
                    result = "zone",
                    notes = "Remove me"
                )

            val updatedRows =
                BoulderRouteRepository.updateBoulderRoute(
                    boulderRouteId = boulderRouteId,
                    difficultyGradeId = null,
                    routeName = null,
                    attempts = 2,
                    result = "top",
                    notes = null
                )

            assertEquals(1, updatedRows)

            val route =
                BoulderRouteRepository.findById(
                    boulderRouteId
                )

            assertNotNull(route)
            assertNull(route.difficultyGradeId)
            assertNull(route.routeName)
            assertNull(route.notes)

        } finally {
            boulderRouteId?.let {
                BoulderRouteRepository.deleteBoulderRoute(it)
            }

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
    fun `allows all valid boulder results`() = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-results")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        val routeIds = mutableListOf<Long>()

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 4)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            val validResults =
                listOf(
                    "attempted",
                    "zone",
                    "top",
                    "flash"
                )

            for (result in validResults) {
                val routeId =
                    BoulderRouteRepository.createBoulderRoute(
                        boulderEntryId = boulderEntryId,
                        attempts = 1,
                        result = result
                    )

                routeIds += routeId

                val route =
                    BoulderRouteRepository.findById(routeId)

                assertNotNull(route)
                assertEquals(result, route.result)
            }

        } finally {
            routeIds.forEach {
                BoulderRouteRepository.deleteBoulderRoute(it)
            }

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
    fun `rejects zero attempts`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-attempts")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 5)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            assertFails {
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    attempts = 0,
                    result = "attempted"
                )
            }

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
    fun `rejects invalid boulder result`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-result-invalid")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 6)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            assertFails {
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    attempts = 1,
                    result = "completed"
                )
            }

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
    fun `rejects route for missing boulder entry`(): Unit = runBlocking {
        initializeTestDatabase()

        assertFails {
            BoulderRouteRepository.createBoulderRoute(
                boulderEntryId = Long.MAX_VALUE,
                attempts = 1,
                result = "attempted"
            )
        }
    }

    @Test
    fun `rejects route for missing difficulty grade`(): Unit = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-grade-missing")

        var trainingDayId: Long? = null
        var boulderEntryId: Long? = null

        try {
            trainingDayId =
                TrainingDayRepository.createTrainingDay(
                    profileId = userId,
                    trainingDate = LocalDate(2026, 11, 7)
                )

            boulderEntryId =
                BoulderEntryRepository.createBoulderEntry(
                    trainingDayId = trainingDayId
                )

            assertFails {
                BoulderRouteRepository.createBoulderRoute(
                    boulderEntryId = boulderEntryId,
                    difficultyGradeId = Long.MAX_VALUE,
                    attempts = 1,
                    result = "attempted"
                )
            }

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
    fun `deletes boulder route`() = runBlocking {
        initializeTestDatabase()

        val userId =
            createTestUserWithProfile("boulder-route-delete")

        val trainingDayId =
            TrainingDayRepository.createTrainingDay(
                profileId = userId,
                trainingDate = LocalDate(2026, 11, 8)
            )

        val boulderEntryId =
            BoulderEntryRepository.createBoulderEntry(
                trainingDayId = trainingDayId
            )

        val boulderRouteId =
            BoulderRouteRepository.createBoulderRoute(
                boulderEntryId = boulderEntryId,
                attempts = 1,
                result = "flash"
            )

        try {
            assertNotNull(
                BoulderRouteRepository.findById(
                    boulderRouteId
                )
            )

            BoulderRouteRepository.deleteBoulderRoute(
                boulderRouteId
            )

            assertNull(
                BoulderRouteRepository.findById(
                    boulderRouteId
                )
            )

        } finally {
            BoulderRouteRepository.deleteBoulderRoute(
                boulderRouteId
            )

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
    fun `returns null for unknown boulder route id`() = runBlocking {
        initializeTestDatabase()

        val route =
            BoulderRouteRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(route)
    }

    @Test
    fun `returns zero when updating unknown boulder route`() = runBlocking {
        initializeTestDatabase()

        val updatedRows =
            BoulderRouteRepository.updateBoulderRoute(
                boulderRouteId = Long.MAX_VALUE,
                difficultyGradeId = null,
                routeName = null,
                attempts = 1,
                result = "attempted",
                notes = null
            )

        assertEquals(0, updatedRows)
    }
}

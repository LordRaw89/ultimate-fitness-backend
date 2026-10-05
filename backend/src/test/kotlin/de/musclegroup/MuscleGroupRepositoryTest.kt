package de.musclegroup

import de.config.DatabaseConfig
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MuscleGroupRepositoryTest {

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

    @Test
    fun `findAll returns seeded muscle groups`() = runBlocking {
        initializeTestDatabase()

        val muscleGroups =
            MuscleGroupRepository.findAll()

        assertTrue(muscleGroups.isNotEmpty())
    }

    @Test
    fun `findById returns existing muscle group`() = runBlocking {
        initializeTestDatabase()

        val muscleGroups =
            MuscleGroupRepository.findAll()

        assertTrue(muscleGroups.isNotEmpty())

        val expected = muscleGroups.first()

        val actual =
            MuscleGroupRepository.findById(
                expected.muscleGroupId
            )

        assertNotNull(actual)

        assertEquals(
            expected.muscleGroupId,
            actual.muscleGroupId
        )

        assertEquals(
            expected.muscleGroupName,
            actual.muscleGroupName
        )
    }

    @Test
    fun `findByName returns existing muscle group`() = runBlocking {
        initializeTestDatabase()

        val muscleGroups =
            MuscleGroupRepository.findAll()

        assertTrue(muscleGroups.isNotEmpty())

        val expected = muscleGroups.first()

        val actual =
            MuscleGroupRepository.findByName(
                expected.muscleGroupName
            )

        assertNotNull(actual)

        assertEquals(
            expected.muscleGroupId,
            actual.muscleGroupId
        )

        assertEquals(
            expected.muscleGroupName,
            actual.muscleGroupName
        )
    }

    @Test
    fun `findById returns null for unknown id`() = runBlocking {
        initializeTestDatabase()

        val muscleGroup =
            MuscleGroupRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(muscleGroup)
    }

    @Test
    fun `findByName returns null for unknown name`() = runBlocking {
        initializeTestDatabase()

        val muscleGroup =
            MuscleGroupRepository.findByName(
                "THIS_MUSCLE_GROUP_DOES_NOT_EXIST"
            )

        assertNull(muscleGroup)
    }
}

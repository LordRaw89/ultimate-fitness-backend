package de.musclegroup

import de.config.DatabaseConfig
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExerciseMuscleGroupRepositoryTest {

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
    fun `findAll returns seeded exercise muscle groups`() = runBlocking {
        initializeTestDatabase()

        val mappings =
            ExerciseMuscleGroupRepository.findAll()

        assertTrue(mappings.isNotEmpty())
    }

    @Test
    fun `findById returns existing exercise muscle group`() = runBlocking {
        initializeTestDatabase()

        val mappings =
            ExerciseMuscleGroupRepository.findAll()

        assertTrue(mappings.isNotEmpty())

        val expected = mappings.first()

        val actual =
            ExerciseMuscleGroupRepository.findById(
                expected.exerciseMuscleGroupId
            )

        assertNotNull(actual)

        assertEquals(
            expected.exerciseMuscleGroupId,
            actual.exerciseMuscleGroupId
        )

        assertEquals(
            expected.exerciseId,
            actual.exerciseId
        )

        assertEquals(
            expected.muscleGroupId,
            actual.muscleGroupId
        )

        assertEquals(
            expected.role,
            actual.role
        )
    }

    @Test
    fun `findByExerciseId returns only mappings for selected exercise`() = runBlocking {
        initializeTestDatabase()

        val allMappings =
            ExerciseMuscleGroupRepository.findAll()

        assertTrue(allMappings.isNotEmpty())

        val selectedExerciseId =
            allMappings.first().exerciseId

        val mappings =
            ExerciseMuscleGroupRepository.findByExerciseId(
                selectedExerciseId
            )

        assertTrue(mappings.isNotEmpty())

        assertTrue(
            mappings.all {
                it.exerciseId == selectedExerciseId
            }
        )
    }

    @Test
    fun `findByMuscleGroupId returns only mappings for selected muscle group`() = runBlocking {
        initializeTestDatabase()

        val allMappings =
            ExerciseMuscleGroupRepository.findAll()

        assertTrue(allMappings.isNotEmpty())

        val selectedMuscleGroupId =
            allMappings.first().muscleGroupId

        val mappings =
            ExerciseMuscleGroupRepository.findByMuscleGroupId(
                selectedMuscleGroupId
            )

        assertTrue(mappings.isNotEmpty())

        assertTrue(
            mappings.all {
                it.muscleGroupId == selectedMuscleGroupId
            }
        )
    }

    @Test
    fun `findById returns null for unknown mapping id`() = runBlocking {
        initializeTestDatabase()

        val mapping =
            ExerciseMuscleGroupRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(mapping)
    }

    @Test
    fun `findByExerciseId returns empty list for unknown exercise`() = runBlocking {
        initializeTestDatabase()

        val mappings =
            ExerciseMuscleGroupRepository.findByExerciseId(
                Long.MAX_VALUE
            )

        assertTrue(mappings.isEmpty())
    }

    @Test
    fun `findByMuscleGroupId returns empty list for unknown muscle group`() = runBlocking {
        initializeTestDatabase()

        val mappings =
            ExerciseMuscleGroupRepository.findByMuscleGroupId(
                Long.MAX_VALUE
            )

        assertTrue(mappings.isEmpty())
    }
}

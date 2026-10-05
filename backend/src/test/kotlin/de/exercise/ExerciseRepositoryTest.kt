package de.exercise

import de.config.DatabaseConfig
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExerciseRepositoryTest {

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
    fun `findAll returns seeded exercises`() = runBlocking {
        initializeTestDatabase()

        val exercises =
            ExerciseRepository.findAll()

        assertTrue(exercises.isNotEmpty())
    }

    @Test
    fun `findById returns existing exercise`() = runBlocking {
        initializeTestDatabase()

        val exercises =
            ExerciseRepository.findAll()

        assertTrue(exercises.isNotEmpty())

        val expected = exercises.first()

        val actual =
            ExerciseRepository.findById(
                expected.exerciseId
            )

        assertNotNull(actual)

        assertEquals(
            expected.exerciseId,
            actual.exerciseId
        )

        assertEquals(
            expected.exerciseName,
            actual.exerciseName
        )

        assertEquals(
            expected.exerciseCategoryId,
            actual.exerciseCategoryId
        )

        assertEquals(
            expected.notes,
            actual.notes
        )
    }

    @Test
    fun `findByCategoryId returns only exercises from selected category`() = runBlocking {
        initializeTestDatabase()

        val allExercises =
            ExerciseRepository.findAll()

        assertTrue(allExercises.isNotEmpty())

        val selectedCategoryId =
            allExercises.first().exerciseCategoryId

        val exercises =
            ExerciseRepository.findByCategoryId(
                selectedCategoryId
            )

        assertTrue(exercises.isNotEmpty())

        assertTrue(
            exercises.all {
                it.exerciseCategoryId == selectedCategoryId
            }
        )
    }

    @Test
    fun `findByName returns exercises with selected name`() = runBlocking {
        initializeTestDatabase()

        val allExercises =
            ExerciseRepository.findAll()

        assertTrue(allExercises.isNotEmpty())

        val selectedName =
            allExercises.first().exerciseName

        val exercises =
            ExerciseRepository.findByName(
                selectedName
            )

        assertTrue(exercises.isNotEmpty())

        assertTrue(
            exercises.all {
                it.exerciseName == selectedName
            }
        )
    }

    @Test
    fun `findById returns null for unknown id`() = runBlocking {
        initializeTestDatabase()

        val exercise =
            ExerciseRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(exercise)
    }

    @Test
    fun `findByCategoryId returns empty list for unknown category`() = runBlocking {
        initializeTestDatabase()

        val exercises =
            ExerciseRepository.findByCategoryId(
                Long.MAX_VALUE
            )

        assertTrue(exercises.isEmpty())
    }

    @Test
    fun `findByName returns empty list for unknown name`() = runBlocking {
        initializeTestDatabase()

        val exercises =
            ExerciseRepository.findByName(
                "THIS_EXERCISE_DOES_NOT_EXIST"
            )

        assertTrue(exercises.isEmpty())
    }
}

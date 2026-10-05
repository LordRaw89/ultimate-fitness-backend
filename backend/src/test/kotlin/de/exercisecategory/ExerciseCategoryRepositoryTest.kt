package de.exercisecategory

import de.config.DatabaseConfig
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExerciseCategoryRepositoryTest {

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
    fun `findAll returns seeded exercise categories`() = runBlocking {
        initializeTestDatabase()

        val categories =
            ExerciseCategoryRepository.findAll()

        assertTrue(categories.isNotEmpty())
    }

    @Test
    fun `findById returns existing exercise category`() = runBlocking {
        initializeTestDatabase()

        val categories =
            ExerciseCategoryRepository.findAll()

        assertTrue(categories.isNotEmpty())

        val expected = categories.first()

        val actual =
            ExerciseCategoryRepository.findById(
                expected.exerciseCategoryId
            )

        assertNotNull(actual)

        assertEquals(
            expected.exerciseCategoryId,
            actual.exerciseCategoryId
        )

        assertEquals(
            expected.exerciseCategoryName,
            actual.exerciseCategoryName
        )
    }

    @Test
    fun `findByName returns existing exercise category`() = runBlocking {
        initializeTestDatabase()

        val categories =
            ExerciseCategoryRepository.findAll()

        assertTrue(categories.isNotEmpty())

        val expected = categories.first()

        val actual =
            ExerciseCategoryRepository.findByName(
                expected.exerciseCategoryName
            )

        assertNotNull(actual)

        assertEquals(
            expected.exerciseCategoryId,
            actual.exerciseCategoryId
        )

        assertEquals(
            expected.exerciseCategoryName,
            actual.exerciseCategoryName
        )
    }

    @Test
    fun `findById returns null for unknown id`() = runBlocking {
        initializeTestDatabase()

        val category =
            ExerciseCategoryRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(category)
    }

    @Test
    fun `findByName returns null for unknown name`() = runBlocking {
        initializeTestDatabase()

        val category =
            ExerciseCategoryRepository.findByName(
                "THIS_CATEGORY_DOES_NOT_EXIST"
            )

        assertNull(category)
    }
}

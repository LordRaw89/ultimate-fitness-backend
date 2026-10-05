package de.difficultygrade

import de.config.DatabaseConfig
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DifficultyGradeRepositoryTest {

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
    fun `findAll returns seeded difficulty grades`() = runBlocking {
        initializeTestDatabase()

        val grades = DifficultyGradeRepository.findAll()

        assertTrue(grades.isNotEmpty())
    }

    @Test
    fun `findById returns existing difficulty grade`() = runBlocking {
        initializeTestDatabase()

        val grades = DifficultyGradeRepository.findAll()

        assertTrue(grades.isNotEmpty())

        val expected = grades.first()

        val actual =
            DifficultyGradeRepository.findById(
                expected.difficultyGradeId
            )

        assertNotNull(actual)

        assertEquals(
            expected.difficultyGradeId,
            actual.difficultyGradeId
        )

        assertEquals(
            expected.difficultyGrade,
            actual.difficultyGrade
        )

        assertEquals(
            expected.difficultyGradeSystem,
            actual.difficultyGradeSystem
        )
    }

    @Test
    fun `findById returns null for unknown id`() = runBlocking {
        initializeTestDatabase()

        val grade =
            DifficultyGradeRepository.findById(
                Long.MAX_VALUE
            )

        assertNull(grade)
    }

    @Test
    fun `findBySystem returns only grades from selected system`() = runBlocking {
        initializeTestDatabase()

        val allGrades =
            DifficultyGradeRepository.findAll()

        assertTrue(allGrades.isNotEmpty())

        val selectedSystem =
            allGrades.first().difficultyGradeSystem

        val grades =
            DifficultyGradeRepository.findBySystem(
                selectedSystem
            )

        assertTrue(grades.isNotEmpty())

        assertTrue(
            grades.all {
                it.difficultyGradeSystem == selectedSystem
            }
        )
    }

    @Test
    fun `findAllSystems returns available systems without duplicates`() = runBlocking {
        initializeTestDatabase()

        val allGrades =
            DifficultyGradeRepository.findAll()

        assertTrue(allGrades.isNotEmpty())

        val expectedSystems =
            allGrades
                .map { it.difficultyGradeSystem }
                .distinct()
                .sorted()

        val actualSystems =
            DifficultyGradeRepository.findAllSystems()

        assertEquals(
            expectedSystems,
            actualSystems
        )
    }

    @Test
    fun `findBySystem returns empty list for unknown system`() = runBlocking {
        initializeTestDatabase()

        val grades =
            DifficultyGradeRepository.findBySystem(
                "THIS_SYSTEM_DOES_NOT_EXIST"
            )

        assertTrue(grades.isEmpty())
    }
}

package de.exercisecategory

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.OffsetDateTime

data class ExerciseCategory(
    val exerciseCategoryId: Long,
    val exerciseCategoryName: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object ExerciseCategoryRepository {

    suspend fun findById(
        exerciseCategoryId: Long
    ): ExerciseCategory? {
        return suspendTransaction {
            ExerciseCategoriesTable
                .selectAll()
                .where {
                    ExerciseCategoriesTable.exerciseCategoryId eq exerciseCategoryId
                }
                .singleOrNull()
                ?.let { row ->
                    ExerciseCategory(
                        exerciseCategoryId =
                            row[ExerciseCategoriesTable.exerciseCategoryId],
                        exerciseCategoryName =
                            row[ExerciseCategoriesTable.exerciseCategoryName],
                        createdAt =
                            row[ExerciseCategoriesTable.createdAt],
                        updatedAt =
                            row[ExerciseCategoriesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByName(
        exerciseCategoryName: String
    ): ExerciseCategory? {
        return suspendTransaction {
            ExerciseCategoriesTable
                .selectAll()
                .where {
                    ExerciseCategoriesTable.exerciseCategoryName eq exerciseCategoryName
                }
                .singleOrNull()
                ?.let { row ->
                    ExerciseCategory(
                        exerciseCategoryId =
                            row[ExerciseCategoriesTable.exerciseCategoryId],
                        exerciseCategoryName =
                            row[ExerciseCategoriesTable.exerciseCategoryName],
                        createdAt =
                            row[ExerciseCategoriesTable.createdAt],
                        updatedAt =
                            row[ExerciseCategoriesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAll(): List<ExerciseCategory> {
        return suspendTransaction {
            ExerciseCategoriesTable
                .selectAll()
                .map { row ->
                    ExerciseCategory(
                        exerciseCategoryId =
                            row[ExerciseCategoriesTable.exerciseCategoryId],
                        exerciseCategoryName =
                            row[ExerciseCategoriesTable.exerciseCategoryName],
                        createdAt =
                            row[ExerciseCategoriesTable.createdAt],
                        updatedAt =
                            row[ExerciseCategoriesTable.updatedAt]
                    )
                }
        }
    }
}

package de.exercise

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.OffsetDateTime

data class Exercise(
    val exerciseId: Long,
    val exerciseName: String,
    val exerciseCategoryId: Long,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object ExerciseRepository {

    suspend fun findById(
        exerciseId: Long
    ): Exercise? {
        return suspendTransaction {
            ExercisesTable
                .selectAll()
                .where {
                    ExercisesTable.exerciseId eq exerciseId
                }
                .singleOrNull()
                ?.let { row ->
                    Exercise(
                        exerciseId =
                            row[ExercisesTable.exerciseId],
                        exerciseName =
                            row[ExercisesTable.exerciseName],
                        exerciseCategoryId =
                            row[ExercisesTable.exerciseCategoryId],
                        notes =
                            row[ExercisesTable.notes],
                        createdAt =
                            row[ExercisesTable.createdAt],
                        updatedAt =
                            row[ExercisesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAll(): List<Exercise> {
        return suspendTransaction {
            ExercisesTable
                .selectAll()
                .map { row ->
                    Exercise(
                        exerciseId =
                            row[ExercisesTable.exerciseId],
                        exerciseName =
                            row[ExercisesTable.exerciseName],
                        exerciseCategoryId =
                            row[ExercisesTable.exerciseCategoryId],
                        notes =
                            row[ExercisesTable.notes],
                        createdAt =
                            row[ExercisesTable.createdAt],
                        updatedAt =
                            row[ExercisesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByCategoryId(
        exerciseCategoryId: Long
    ): List<Exercise> {
        return suspendTransaction {
            ExercisesTable
                .selectAll()
                .where {
                    ExercisesTable.exerciseCategoryId eq exerciseCategoryId
                }
                .map { row ->
                    Exercise(
                        exerciseId =
                            row[ExercisesTable.exerciseId],
                        exerciseName =
                            row[ExercisesTable.exerciseName],
                        exerciseCategoryId =
                            row[ExercisesTable.exerciseCategoryId],
                        notes =
                            row[ExercisesTable.notes],
                        createdAt =
                            row[ExercisesTable.createdAt],
                        updatedAt =
                            row[ExercisesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByName(
        exerciseName: String
    ): List<Exercise> {
        return suspendTransaction {
            ExercisesTable
                .selectAll()
                .where {
                    ExercisesTable.exerciseName eq exerciseName
                }
                .map { row ->
                    Exercise(
                        exerciseId =
                            row[ExercisesTable.exerciseId],
                        exerciseName =
                            row[ExercisesTable.exerciseName],
                        exerciseCategoryId =
                            row[ExercisesTable.exerciseCategoryId],
                        notes =
                            row[ExercisesTable.notes],
                        createdAt =
                            row[ExercisesTable.createdAt],
                        updatedAt =
                            row[ExercisesTable.updatedAt]
                    )
                }
        }
    }
}

package de.musclegroup

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.OffsetDateTime

data class ExerciseMuscleGroup(
    val exerciseMuscleGroupId: Long,
    val exerciseId: Long,
    val muscleGroupId: Long,
    val role: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object ExerciseMuscleGroupRepository {

    suspend fun findById(
        exerciseMuscleGroupId: Long
    ): ExerciseMuscleGroup? {
        return suspendTransaction {
            ExerciseMuscleGroupsTable
                .selectAll()
                .where {
                    ExerciseMuscleGroupsTable.exerciseMuscleGroupId eq
                            exerciseMuscleGroupId
                }
                .singleOrNull()
                ?.let { row ->
                    ExerciseMuscleGroup(
                        exerciseMuscleGroupId =
                            row[ExerciseMuscleGroupsTable.exerciseMuscleGroupId],
                        exerciseId =
                            row[ExerciseMuscleGroupsTable.exerciseId],
                        muscleGroupId =
                            row[ExerciseMuscleGroupsTable.muscleGroupId],
                        role =
                            row[ExerciseMuscleGroupsTable.role],
                        createdAt =
                            row[ExerciseMuscleGroupsTable.createdAt],
                        updatedAt =
                            row[ExerciseMuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAll(): List<ExerciseMuscleGroup> {
        return suspendTransaction {
            ExerciseMuscleGroupsTable
                .selectAll()
                .map { row ->
                    ExerciseMuscleGroup(
                        exerciseMuscleGroupId =
                            row[ExerciseMuscleGroupsTable.exerciseMuscleGroupId],
                        exerciseId =
                            row[ExerciseMuscleGroupsTable.exerciseId],
                        muscleGroupId =
                            row[ExerciseMuscleGroupsTable.muscleGroupId],
                        role =
                            row[ExerciseMuscleGroupsTable.role],
                        createdAt =
                            row[ExerciseMuscleGroupsTable.createdAt],
                        updatedAt =
                            row[ExerciseMuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByExerciseId(
        exerciseId: Long
    ): List<ExerciseMuscleGroup> {
        return suspendTransaction {
            ExerciseMuscleGroupsTable
                .selectAll()
                .where {
                    ExerciseMuscleGroupsTable.exerciseId eq exerciseId
                }
                .map { row ->
                    ExerciseMuscleGroup(
                        exerciseMuscleGroupId =
                            row[ExerciseMuscleGroupsTable.exerciseMuscleGroupId],
                        exerciseId =
                            row[ExerciseMuscleGroupsTable.exerciseId],
                        muscleGroupId =
                            row[ExerciseMuscleGroupsTable.muscleGroupId],
                        role =
                            row[ExerciseMuscleGroupsTable.role],
                        createdAt =
                            row[ExerciseMuscleGroupsTable.createdAt],
                        updatedAt =
                            row[ExerciseMuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByMuscleGroupId(
        muscleGroupId: Long
    ): List<ExerciseMuscleGroup> {
        return suspendTransaction {
            ExerciseMuscleGroupsTable
                .selectAll()
                .where {
                    ExerciseMuscleGroupsTable.muscleGroupId eq muscleGroupId
                }
                .map { row ->
                    ExerciseMuscleGroup(
                        exerciseMuscleGroupId =
                            row[ExerciseMuscleGroupsTable.exerciseMuscleGroupId],
                        exerciseId =
                            row[ExerciseMuscleGroupsTable.exerciseId],
                        muscleGroupId =
                            row[ExerciseMuscleGroupsTable.muscleGroupId],
                        role =
                            row[ExerciseMuscleGroupsTable.role],
                        createdAt =
                            row[ExerciseMuscleGroupsTable.createdAt],
                        updatedAt =
                            row[ExerciseMuscleGroupsTable.updatedAt]
                    )
                }
        }
    }
}

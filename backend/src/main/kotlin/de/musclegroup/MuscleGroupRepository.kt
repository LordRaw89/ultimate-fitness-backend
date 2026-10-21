package de.musclegroup

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.OffsetDateTime

data class MuscleGroup(
    val muscleGroupId: Long,
    val muscleGroupName: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object MuscleGroupRepository {

    suspend fun findById(
        muscleGroupId: Long
    ): MuscleGroup? {
        return suspendTransaction {
            MuscleGroupsTable
                .selectAll()
                .where {
                    MuscleGroupsTable.muscleGroupId eq muscleGroupId
                }
                .singleOrNull()
                ?.let { row ->
                    MuscleGroup(
                        muscleGroupId =
                            row[MuscleGroupsTable.muscleGroupId],
                        muscleGroupName =
                            row[MuscleGroupsTable.muscleGroupName],
                        createdAt =
                            row[MuscleGroupsTable.createdAt],
                        updatedAt =
                            row[MuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByIds(
        muscleGroupIds: List<Long>
    ): List<MuscleGroup> {

        if (muscleGroupIds.isEmpty()) {
            return emptyList()
        }

        return suspendTransaction {
            MuscleGroupsTable
                .selectAll()
                .where {
                    MuscleGroupsTable.muscleGroupId inList muscleGroupIds
                }
                .map { row ->
                    MuscleGroup(
                        muscleGroupId =
                            row[MuscleGroupsTable.muscleGroupId],
                        muscleGroupName =
                            row[MuscleGroupsTable.muscleGroupName],
                        createdAt =
                            row[MuscleGroupsTable.createdAt],
                        updatedAt =
                            row[MuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findByName(
        muscleGroupName: String
    ): MuscleGroup? {
        return suspendTransaction {
            MuscleGroupsTable
                .selectAll()
                .where {
                    MuscleGroupsTable.muscleGroupName eq muscleGroupName
                }
                .singleOrNull()
                ?.let { row ->
                    MuscleGroup(
                        muscleGroupId =
                            row[MuscleGroupsTable.muscleGroupId],
                        muscleGroupName =
                            row[MuscleGroupsTable.muscleGroupName],
                        createdAt =
                            row[MuscleGroupsTable.createdAt],
                        updatedAt =
                            row[MuscleGroupsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAll(): List<MuscleGroup> {
        return suspendTransaction {
            MuscleGroupsTable
                .selectAll()
                .map { row ->
                    MuscleGroup(
                        muscleGroupId =
                            row[MuscleGroupsTable.muscleGroupId],
                        muscleGroupName =
                            row[MuscleGroupsTable.muscleGroupName],
                        createdAt =
                            row[MuscleGroupsTable.createdAt],
                        updatedAt =
                            row[MuscleGroupsTable.updatedAt]
                    )
                }
        }
    }
}

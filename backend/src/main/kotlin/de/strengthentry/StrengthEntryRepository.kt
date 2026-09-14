package de.strengthentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime

data class StrengthEntry(
    val strengthEntryId: Long,
    val trainingDayId: Long,
    val exerciseId: Long,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object StrengthEntryRepository {

    suspend fun createStrengthEntry(
        trainingDayId: Long,
        exerciseId: Long,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = StrengthEntriesTable.insert {
                it[StrengthEntriesTable.trainingDayId] = trainingDayId
                it[StrengthEntriesTable.exerciseId] = exerciseId
                it[StrengthEntriesTable.notes] = notes
            }

            statement[StrengthEntriesTable.strengthEntryId]
        }
    }

    suspend fun findById(strengthEntryId: Long): StrengthEntry? {
        return suspendTransaction {
            StrengthEntriesTable
                .selectAll()
                .where {
                    StrengthEntriesTable.strengthEntryId eq strengthEntryId
                }
                .singleOrNull()
                ?.let { row ->
                    StrengthEntry(
                        strengthEntryId =
                            row[StrengthEntriesTable.strengthEntryId],
                        trainingDayId =
                            row[StrengthEntriesTable.trainingDayId],
                        exerciseId =
                            row[StrengthEntriesTable.exerciseId],
                        notes =
                            row[StrengthEntriesTable.notes],
                        createdAt =
                            row[StrengthEntriesTable.createdAt],
                        updatedAt =
                            row[StrengthEntriesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateNotes(
        strengthEntryId: Long,
        notes: String?
    ): Int {
        return suspendTransaction {
            StrengthEntriesTable.update(
                where = {
                    StrengthEntriesTable.strengthEntryId eq strengthEntryId
                }
            ) {
                it[StrengthEntriesTable.notes] = notes
                it[StrengthEntriesTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteStrengthEntry(
        strengthEntryId: Long
    ) {
        suspendTransaction {
            StrengthEntriesTable.deleteWhere {
                StrengthEntriesTable.strengthEntryId eq strengthEntryId
            }
        }
    }
}

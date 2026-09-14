package de.enduranceentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime

data class EnduranceEntry(
    val enduranceEntryId: Long,
    val trainingDayId: Long,
    val exerciseId: Long,
    val durationSec: Int,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object EnduranceEntryRepository {

    suspend fun createEnduranceEntry(
        trainingDayId: Long,
        exerciseId: Long,
        durationSec: Int,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = EnduranceEntriesTable.insert {
                it[EnduranceEntriesTable.trainingDayId] = trainingDayId
                it[EnduranceEntriesTable.exerciseId] = exerciseId
                it[EnduranceEntriesTable.durationSec] = durationSec
                it[EnduranceEntriesTable.notes] = notes
            }

            statement[EnduranceEntriesTable.enduranceEntryId]
        }
    }

    suspend fun findById(enduranceEntryId: Long): EnduranceEntry? {
        return suspendTransaction {
            EnduranceEntriesTable
                .selectAll()
                .where {
                    EnduranceEntriesTable.enduranceEntryId eq enduranceEntryId
                }
                .singleOrNull()
                ?.let { row ->
                    EnduranceEntry(
                        enduranceEntryId =
                            row[EnduranceEntriesTable.enduranceEntryId],
                        trainingDayId =
                            row[EnduranceEntriesTable.trainingDayId],
                        exerciseId =
                            row[EnduranceEntriesTable.exerciseId],
                        durationSec =
                            row[EnduranceEntriesTable.durationSec],
                        notes =
                            row[EnduranceEntriesTable.notes],
                        createdAt =
                            row[EnduranceEntriesTable.createdAt],
                        updatedAt =
                            row[EnduranceEntriesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateEnduranceEntry(
        enduranceEntryId: Long,
        durationSec: Int,
        notes: String?
    ): Int {
        return suspendTransaction {
            EnduranceEntriesTable.update(
                where = {
                    EnduranceEntriesTable.enduranceEntryId eq enduranceEntryId
                }
            ) {
                it[EnduranceEntriesTable.durationSec] = durationSec
                it[EnduranceEntriesTable.notes] = notes
                it[EnduranceEntriesTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteEnduranceEntry(enduranceEntryId: Long) {
        suspendTransaction {
            EnduranceEntriesTable.deleteWhere {
                EnduranceEntriesTable.enduranceEntryId eq enduranceEntryId
            }
        }
    }
}

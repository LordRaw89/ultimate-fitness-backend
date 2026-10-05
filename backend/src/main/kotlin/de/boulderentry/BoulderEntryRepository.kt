package de.boulderentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime

data class BoulderEntry(
    val boulderEntryId: Long,
    val trainingDayId: Long,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object BoulderEntryRepository {

    suspend fun createBoulderEntry(
        trainingDayId: Long,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = BoulderEntriesTable.insert {
                it[BoulderEntriesTable.trainingDayId] = trainingDayId
                it[BoulderEntriesTable.notes] = notes
            }

            statement[BoulderEntriesTable.boulderEntryId]
        }
    }

    suspend fun findById(boulderEntryId: Long): BoulderEntry? {
        return suspendTransaction {
            BoulderEntriesTable
                .selectAll()
                .where {
                    BoulderEntriesTable.boulderEntryId eq boulderEntryId
                }
                .singleOrNull()
                ?.let { row ->
                    BoulderEntry(
                        boulderEntryId =
                            row[BoulderEntriesTable.boulderEntryId],
                        trainingDayId =
                            row[BoulderEntriesTable.trainingDayId],
                        notes =
                            row[BoulderEntriesTable.notes],
                        createdAt =
                            row[BoulderEntriesTable.createdAt],
                        updatedAt =
                            row[BoulderEntriesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateNotes(
        boulderEntryId: Long,
        notes: String?
    ): Int {
        return suspendTransaction {
            BoulderEntriesTable.update(
                where = {
                    BoulderEntriesTable.boulderEntryId eq boulderEntryId
                }
            ) {
                it[BoulderEntriesTable.notes] = notes
                it[BoulderEntriesTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteBoulderEntry(boulderEntryId: Long) {
        suspendTransaction {
            BoulderEntriesTable.deleteWhere {
                BoulderEntriesTable.boulderEntryId eq boulderEntryId
            }
        }
    }
}

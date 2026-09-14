package de.strengthentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime

data class StrengthTimeSet(
    val strengthTimeSetId: Long,
    val strengthEntryId: Long,
    val setNumber: Short,
    val durationSec: Int,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object StrengthTimeSetRepository {

    suspend fun createStrengthTimeSet(
        strengthEntryId: Long,
        setNumber: Short,
        durationSec: Int,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = StrengthTimeSetsTable.insert {
                it[StrengthTimeSetsTable.strengthEntryId] = strengthEntryId
                it[StrengthTimeSetsTable.setNumber] = setNumber
                it[StrengthTimeSetsTable.durationSec] = durationSec
                it[StrengthTimeSetsTable.notes] = notes
            }

            statement[StrengthTimeSetsTable.strengthTimeSetId]
        }
    }

    suspend fun findById(strengthTimeSetId: Long): StrengthTimeSet? {
        return suspendTransaction {
            StrengthTimeSetsTable
                .selectAll()
                .where {
                    StrengthTimeSetsTable.strengthTimeSetId eq strengthTimeSetId
                }
                .singleOrNull()
                ?.let { row ->
                    StrengthTimeSet(
                        strengthTimeSetId =
                            row[StrengthTimeSetsTable.strengthTimeSetId],
                        strengthEntryId =
                            row[StrengthTimeSetsTable.strengthEntryId],
                        setNumber =
                            row[StrengthTimeSetsTable.setNumber],
                        durationSec =
                            row[StrengthTimeSetsTable.durationSec],
                        notes =
                            row[StrengthTimeSetsTable.notes],
                        createdAt =
                            row[StrengthTimeSetsTable.createdAt],
                        updatedAt =
                            row[StrengthTimeSetsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateStrengthTimeSet(
        strengthTimeSetId: Long,
        setNumber: Short,
        durationSec: Int,
        notes: String?
    ): Int {
        return suspendTransaction {
            StrengthTimeSetsTable.update(
                where = {
                    StrengthTimeSetsTable.strengthTimeSetId eq strengthTimeSetId
                }
            ) {
                it[StrengthTimeSetsTable.setNumber] = setNumber
                it[StrengthTimeSetsTable.durationSec] = durationSec
                it[StrengthTimeSetsTable.notes] = notes
                it[StrengthTimeSetsTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteStrengthTimeSet(strengthTimeSetId: Long) {
        suspendTransaction {
            StrengthTimeSetsTable.deleteWhere {
                StrengthTimeSetsTable.strengthTimeSetId eq strengthTimeSetId
            }
        }
    }
}

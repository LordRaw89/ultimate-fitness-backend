package de.strengthentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.math.BigDecimal
import java.time.OffsetDateTime

data class StrengthRepSet(
    val strengthRepSetId: Long,
    val strengthEntryId: Long,
    val setNumber: Short,
    val reps: Short,
    val weightKg: BigDecimal,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object StrengthRepSetRepository {

    suspend fun createStrengthRepSet(
        strengthEntryId: Long,
        setNumber: Short,
        reps: Short,
        weightKg: BigDecimal,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = StrengthRepSetsTable.insert {
                it[StrengthRepSetsTable.strengthEntryId] = strengthEntryId
                it[StrengthRepSetsTable.setNumber] = setNumber
                it[StrengthRepSetsTable.reps] = reps
                it[StrengthRepSetsTable.weightKg] = weightKg
                it[StrengthRepSetsTable.notes] = notes
            }

            statement[StrengthRepSetsTable.strengthRepSetId]
        }
    }

    suspend fun findById(strengthRepSetId: Long): StrengthRepSet? {
        return suspendTransaction {
            StrengthRepSetsTable
                .selectAll()
                .where {
                    StrengthRepSetsTable.strengthRepSetId eq strengthRepSetId
                }
                .singleOrNull()
                ?.let { row ->
                    StrengthRepSet(
                        strengthRepSetId =
                            row[StrengthRepSetsTable.strengthRepSetId],
                        strengthEntryId =
                            row[StrengthRepSetsTable.strengthEntryId],
                        setNumber =
                            row[StrengthRepSetsTable.setNumber],
                        reps =
                            row[StrengthRepSetsTable.reps],
                        weightKg =
                            row[StrengthRepSetsTable.weightKg],
                        notes =
                            row[StrengthRepSetsTable.notes],
                        createdAt =
                            row[StrengthRepSetsTable.createdAt],
                        updatedAt =
                            row[StrengthRepSetsTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateStrengthRepSet(
        strengthRepSetId: Long,
        setNumber: Short,
        reps: Short,
        weightKg: BigDecimal,
        notes: String?
    ): Int {
        return suspendTransaction {
            StrengthRepSetsTable.update(
                where = {
                    StrengthRepSetsTable.strengthRepSetId eq strengthRepSetId
                }
            ) {
                it[StrengthRepSetsTable.setNumber] = setNumber
                it[StrengthRepSetsTable.reps] = reps
                it[StrengthRepSetsTable.weightKg] = weightKg
                it[StrengthRepSetsTable.notes] = notes
                it[StrengthRepSetsTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteStrengthRepSet(strengthRepSetId: Long) {
        suspendTransaction {
            StrengthRepSetsTable.deleteWhere {
                StrengthRepSetsTable.strengthRepSetId eq strengthRepSetId
            }
        }
    }
}

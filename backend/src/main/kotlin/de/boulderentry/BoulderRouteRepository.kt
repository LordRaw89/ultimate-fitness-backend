package de.boulderentry

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime

data class BoulderRoute(
    val boulderRouteId: Long,
    val boulderEntryId: Long,
    val difficultyGradeId: Long?,
    val routeName: String?,
    val attempts: Short,
    val result: String,
    val notes: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object BoulderRouteRepository {

    suspend fun createBoulderRoute(
        boulderEntryId: Long,
        difficultyGradeId: Long? = null,
        routeName: String? = null,
        attempts: Short,
        result: String,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = BoulderRoutesTable.insert {
                it[BoulderRoutesTable.boulderEntryId] = boulderEntryId
                it[BoulderRoutesTable.difficultyGradeId] = difficultyGradeId
                it[BoulderRoutesTable.routeName] = routeName
                it[BoulderRoutesTable.attempts] = attempts
                it[BoulderRoutesTable.result] = result
                it[BoulderRoutesTable.notes] = notes
            }

            statement[BoulderRoutesTable.boulderRouteId]
        }
    }

    suspend fun findById(boulderRouteId: Long): BoulderRoute? {
        return suspendTransaction {
            BoulderRoutesTable
                .selectAll()
                .where {
                    BoulderRoutesTable.boulderRouteId eq boulderRouteId
                }
                .singleOrNull()
                ?.let { row ->
                    BoulderRoute(
                        boulderRouteId =
                            row[BoulderRoutesTable.boulderRouteId],
                        boulderEntryId =
                            row[BoulderRoutesTable.boulderEntryId],
                        difficultyGradeId =
                            row[BoulderRoutesTable.difficultyGradeId],
                        routeName =
                            row[BoulderRoutesTable.routeName],
                        attempts =
                            row[BoulderRoutesTable.attempts],
                        result =
                            row[BoulderRoutesTable.result],
                        notes =
                            row[BoulderRoutesTable.notes],
                        createdAt =
                            row[BoulderRoutesTable.createdAt],
                        updatedAt =
                            row[BoulderRoutesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun updateBoulderRoute(
        boulderRouteId: Long,
        difficultyGradeId: Long?,
        routeName: String?,
        attempts: Short,
        result: String,
        notes: String?
    ): Int {
        return suspendTransaction {
            BoulderRoutesTable.update(
                where = {
                    BoulderRoutesTable.boulderRouteId eq boulderRouteId
                }
            ) {
                it[BoulderRoutesTable.difficultyGradeId] =
                    difficultyGradeId

                it[BoulderRoutesTable.routeName] =
                    routeName

                it[BoulderRoutesTable.attempts] =
                    attempts

                it[BoulderRoutesTable.result] =
                    result

                it[BoulderRoutesTable.notes] =
                    notes

                it[BoulderRoutesTable.updatedAt] =
                    OffsetDateTime.now()
            }
        }
    }

    suspend fun deleteBoulderRoute(boulderRouteId: Long) {
        suspendTransaction {
            BoulderRoutesTable.deleteWhere {
                BoulderRoutesTable.boulderRouteId eq boulderRouteId
            }
        }
    }
}

package de.trainingday

import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime
import java.util.UUID

data class TrainingDay(
    val trainingDayId: Long,
    val profileId: UUID,
    val trainingDate: LocalDate,
    val notes: String?,
    val updatedAt: OffsetDateTime
)

object TrainingDayRepository {

    suspend fun createTrainingDay(
        profileId: UUID,
        trainingDate: LocalDate,
        notes: String? = null
    ): Long {
        return suspendTransaction {
            val statement = TrainingDaysTable.insert {
                it[TrainingDaysTable.profileId] = profileId
                it[TrainingDaysTable.trainingDate] = trainingDate
                it[TrainingDaysTable.notes] = notes
            }

            statement[TrainingDaysTable.trainingDayId]
        }
    }

    suspend fun findById(trainingDayId: Long): TrainingDay? {
        return suspendTransaction {
            TrainingDaysTable
                .selectAll()
                .where { TrainingDaysTable.trainingDayId eq trainingDayId }
                .singleOrNull()
                ?.let { row ->
                    TrainingDay(
                        trainingDayId = row[TrainingDaysTable.trainingDayId],
                        profileId = row[TrainingDaysTable.profileId],
                        trainingDate = row[TrainingDaysTable.trainingDate],
                        notes = row[TrainingDaysTable.notes],
                        updatedAt = row[TrainingDaysTable.updatedAt]
                    )
                }
        }
    }

    suspend fun deleteTrainingDay(trainingDayId: Long) {
        suspendTransaction {
            TrainingDaysTable.deleteWhere {
                TrainingDaysTable.trainingDayId eq trainingDayId
            }
        }
    }

    suspend fun updateNotes(
        trainingDayId: Long,
        notes: String?
    ): Int {
        return suspendTransaction {
            TrainingDaysTable.update(
                where = { TrainingDaysTable.trainingDayId eq trainingDayId }
            ) {
                it[TrainingDaysTable.notes] = notes
                it[TrainingDaysTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }
}

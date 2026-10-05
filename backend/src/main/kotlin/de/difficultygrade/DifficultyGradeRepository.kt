package de.difficultygrade

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.OffsetDateTime

data class DifficultyGrade(
    val difficultyGradeId: Long,
    val difficultyGrade: String,
    val difficultyGradeSystem: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

object DifficultyGradeRepository {

    suspend fun findById(
        difficultyGradeId: Long
    ): DifficultyGrade? {
        return suspendTransaction {
            DifficultyGradesTable
                .selectAll()
                .where {
                    DifficultyGradesTable.difficultyGradeId eq difficultyGradeId
                }
                .singleOrNull()
                ?.let { row ->
                    DifficultyGrade(
                        difficultyGradeId =
                            row[DifficultyGradesTable.difficultyGradeId],
                        difficultyGrade =
                            row[DifficultyGradesTable.difficultyGrade],
                        difficultyGradeSystem =
                            row[DifficultyGradesTable.difficultyGradeSystem],
                        createdAt =
                            row[DifficultyGradesTable.createdAt],
                        updatedAt =
                            row[DifficultyGradesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAll(): List<DifficultyGrade> {
        return suspendTransaction {
            DifficultyGradesTable
                .selectAll()
                .map { row ->
                    DifficultyGrade(
                        difficultyGradeId =
                            row[DifficultyGradesTable.difficultyGradeId],
                        difficultyGrade =
                            row[DifficultyGradesTable.difficultyGrade],
                        difficultyGradeSystem =
                            row[DifficultyGradesTable.difficultyGradeSystem],
                        createdAt =
                            row[DifficultyGradesTable.createdAt],
                        updatedAt =
                            row[DifficultyGradesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findBySystem(
        system: String
    ): List<DifficultyGrade> {
        return suspendTransaction {
            DifficultyGradesTable
                .selectAll()
                .where {
                    DifficultyGradesTable.difficultyGradeSystem eq system
                }
                .map { row ->
                    DifficultyGrade(
                        difficultyGradeId =
                            row[DifficultyGradesTable.difficultyGradeId],
                        difficultyGrade =
                            row[DifficultyGradesTable.difficultyGrade],
                        difficultyGradeSystem =
                            row[DifficultyGradesTable.difficultyGradeSystem],
                        createdAt =
                            row[DifficultyGradesTable.createdAt],
                        updatedAt =
                            row[DifficultyGradesTable.updatedAt]
                    )
                }
        }
    }

    suspend fun findAllSystems(): List<String> {
        return suspendTransaction {
            DifficultyGradesTable
                .selectAll()
                .map {
                    it[DifficultyGradesTable.difficultyGradeSystem]
                }
                .distinct()
                .sorted()
        }
    }
}

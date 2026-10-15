package de.difficultygrade

import kotlinx.serialization.Serializable

@Serializable
data class DifficultyGradeResponse(
    val id: Long,
    val difficultyGrade: String,
    val difficultyGradeSystem: String
)

fun DifficultyGrade.toResponse(): DifficultyGradeResponse {
    return DifficultyGradeResponse(
        id = difficultyGradeId,
        difficultyGrade = difficultyGrade,
        difficultyGradeSystem = difficultyGradeSystem
    )
}

package de.exercisecategory

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseCategoryResponse(
    val id: Long,
    val name: String
)

fun ExerciseCategory.toResponse(): ExerciseCategoryResponse {
    return ExerciseCategoryResponse(
        id = exerciseCategoryId,
        name = exerciseCategoryName
    )
}

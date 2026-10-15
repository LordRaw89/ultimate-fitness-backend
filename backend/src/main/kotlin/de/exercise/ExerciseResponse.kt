package de.exercise

import de.musclegroup.MuscleGroup
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseResponse(
    val id: Long,
    val name: String,
    val exerciseCategoryId: Long,
    val notes: String?
)

@Serializable
data class MuscleGroupResponse(
    val id: Long,
    val name: String
)

@Serializable
data class ExerciseMuscleGroupResponse(
    val muscleGroup: MuscleGroupResponse,
    val role: String
)

@Serializable
data class ExerciseWithMuscleGroupsResponse(
    val id: Long,
    val name: String,
    val exerciseCategoryId: Long,
    val notes: String?,
    val muscleGroups: List<ExerciseMuscleGroupResponse>
)

fun Exercise.toResponse(): ExerciseResponse {
    return ExerciseResponse(
        id = exerciseId,
        name = exerciseName,
        exerciseCategoryId = exerciseCategoryId,
        notes = notes
    )
}

fun MuscleGroup.toResponse(): MuscleGroupResponse {
    return MuscleGroupResponse(
        id = muscleGroupId,
        name = muscleGroupName
    )
}

fun ExerciseMuscleGroupDetails.toResponse(): ExerciseMuscleGroupResponse {
    return ExerciseMuscleGroupResponse(
        muscleGroup = muscleGroup.toResponse(),
        role = role
    )
}

fun ExerciseWithMuscleGroups.toResponse(): ExerciseWithMuscleGroupsResponse {
    return ExerciseWithMuscleGroupsResponse(
        id = exercise.exerciseId,
        name = exercise.exerciseName,
        exerciseCategoryId = exercise.exerciseCategoryId,
        notes = exercise.notes,
        muscleGroups = muscleGroups.map { it.toResponse() }
    )
}

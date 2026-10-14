package de.musclegroup

import kotlinx.serialization.Serializable

@Serializable
data class MuscleGroupResponse(
    val id: Long,
    val name: String
)

fun MuscleGroup.toResponse(): MuscleGroupResponse {
    return MuscleGroupResponse(
        id = muscleGroupId,
        name = muscleGroupName
    )
}

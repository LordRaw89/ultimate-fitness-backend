package de.musclegroup

object MuscleGroupService {

    suspend fun getMuscleGroupById(
        muscleGroupId: Long
    ): MuscleGroup? {
        return MuscleGroupRepository.findById(muscleGroupId)
    }

    suspend fun getMuscleGroupByName(
        muscleGroupName: String
    ): MuscleGroup? {
        return MuscleGroupRepository.findByName(muscleGroupName)
    }

    suspend fun getAllMuscleGroups(): List<MuscleGroup> {
        return MuscleGroupRepository.findAll()
    }

}
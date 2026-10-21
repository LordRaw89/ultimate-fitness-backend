package de.exercise

import de.musclegroup.ExerciseMuscleGroupRepository
import de.musclegroup.MuscleGroup
import de.musclegroup.MuscleGroupRepository

data class ExerciseMuscleGroupDetails(
    val muscleGroup: MuscleGroup,
    val role: String
)

data class ExerciseWithMuscleGroups(
    val exercise: Exercise,
    val muscleGroups: List<ExerciseMuscleGroupDetails>
)

object ExerciseService {

    suspend fun getAllExercises(): List<Exercise> {
        return ExerciseRepository.findAll()
    }

    suspend fun getExerciseById(
        exerciseId: Long
    ): Exercise? {
        return ExerciseRepository.findById(exerciseId)
    }

    suspend fun getExercisesByCategoryId(
        exerciseCategoryId: Long
    ): List<Exercise> {
        return ExerciseRepository.findByCategoryId(exerciseCategoryId)
    }

    suspend fun getExerciseWithMuscleGroupsById(
        exerciseId: Long
    ): ExerciseWithMuscleGroups? {

        val exercise = ExerciseRepository.findById(exerciseId)
            ?: return null

        val exerciseMuscleGroups =
            ExerciseMuscleGroupRepository.findByExerciseId(exerciseId)

        val muscleGroupDetails = mutableListOf<ExerciseMuscleGroupDetails>()

        for (association in exerciseMuscleGroups) {
            val muscleGroup = MuscleGroupRepository.findById(
                association.muscleGroupId
            )

            if (muscleGroup != null) {
                muscleGroupDetails.add(
                    ExerciseMuscleGroupDetails(
                        muscleGroup = muscleGroup,
                        role = association.role
                    )
                )
            }
        }

        return ExerciseWithMuscleGroups(
            exercise = exercise,
            muscleGroups = muscleGroupDetails
        )
    }


    suspend fun getExercisesWithMuscleGroupsByIds(
        exerciseIds: List<Long>
    ): List<ExerciseWithMuscleGroups> {

        if (exerciseIds.isEmpty()) {
            return emptyList()
        }

        val exercises = ExerciseRepository.findByIds(exerciseIds)

        if (exercises.isEmpty()) {
            return emptyList()
        }

        val exerciseMuscleGroups =
            ExerciseMuscleGroupRepository.findByExerciseIds(
                exercises.map { it.exerciseId }
            )

        val muscleGroupIds = exerciseMuscleGroups
            .map { it.muscleGroupId }
            .distinct()

        val muscleGroups = MuscleGroupRepository.findByIds(muscleGroupIds)

        val associationsByExerciseId =
            exerciseMuscleGroups.groupBy { it.exerciseId }

        val muscleGroupsById =
            muscleGroups.associateBy { it.muscleGroupId }

        return exercises.map { exercise ->

            val associations =
                associationsByExerciseId[exercise.exerciseId]
                    ?: emptyList()

            val details = associations.mapNotNull { association ->
                val muscleGroup =
                    muscleGroupsById[association.muscleGroupId]

                muscleGroup?.let {
                    ExerciseMuscleGroupDetails(
                        muscleGroup = it,
                        role = association.role
                    )
                }
            }

            ExerciseWithMuscleGroups(
                exercise = exercise,
                muscleGroups = details
            )
        }
    }

}

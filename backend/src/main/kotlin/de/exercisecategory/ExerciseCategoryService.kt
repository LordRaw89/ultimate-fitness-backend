package de.exercisecategory

object ExerciseCategoryService {

    suspend fun getAllCategories(): List<ExerciseCategory> {
        return ExerciseCategoryRepository.findAll()
    }

    suspend fun getCategoryById(
        exerciseCategoryId: Long
    ): ExerciseCategory? {
        return ExerciseCategoryRepository.findById(exerciseCategoryId)
    }
}

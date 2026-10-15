package de.difficultygrade

object DifficultyGradeService {

    suspend fun getAllDifficultyGrades(): List<DifficultyGrade> {
        return DifficultyGradeRepository.findAll()
    }

    suspend fun getDifficultyGradeById(
        difficultyGradeId: Long
    ): DifficultyGrade? {
        return DifficultyGradeRepository.findById(difficultyGradeId)
    }
}

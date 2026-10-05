package de.difficultygrade

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object DifficultyGradesTable : Table("public.difficulty_grades") {

    val difficultyGradeId = long("difficulty_grade_id").autoIncrement()

    val difficultyGrade = text("difficulty_grade")

    val difficultyGradeSystem = text("difficulty_grade_system")

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(difficultyGradeId)
}

package de.exercise

import de.exercisecategory.ExerciseCategoriesTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object ExercisesTable : Table("public.exercises") {

    val exerciseId = long("exercise_id").autoIncrement()

    val exerciseName = text("exercise_name")

    val exerciseCategoryId = reference(
        "exercise_category_id",
        ExerciseCategoriesTable.exerciseCategoryId
    )

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(exerciseId)
}

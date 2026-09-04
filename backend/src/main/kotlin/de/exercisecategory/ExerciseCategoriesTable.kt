package de.exercisecategory

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object ExerciseCategoriesTable : Table("public.exercise_categories"){

    val exerciseCategoryId = long("exercise_category_id")

    val exerciseCategoryName = text("exercise_category_name")

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(exerciseCategoryId)
}
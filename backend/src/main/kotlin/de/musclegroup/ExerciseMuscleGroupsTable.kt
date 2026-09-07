package de.musclegroup

import de.exercise.ExercisesTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object ExerciseMuscleGroupsTable : Table("public.exercise_muscle_groups") {

    val exerciseMuscleGroupId = long("exercise_muscle_group_id")

    val exerciseId = reference(
        "exercise_id",
        ExercisesTable.exerciseId
    )

    val muscleGroupId = reference(
        "muscle_group_id",
        MuscleGroupsTable.muscleGroupId
    )

    val role = text("role")

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(exerciseMuscleGroupId)
}

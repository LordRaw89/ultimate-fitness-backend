package de.strengthentry

import de.exercise.ExercisesTable
import de.trainingday.TrainingDaysTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object StrengthEntriesTable : Table("public.strength_entries") {

    val strengthEntryId = long("strength_entry_id")

    val trainingDayId = reference(
        "training_day_id",
        TrainingDaysTable.trainingDayId
    )

    val exerciseId = reference(
        "exercise_id",
        ExercisesTable.exerciseId
    )

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(strengthEntryId)
}

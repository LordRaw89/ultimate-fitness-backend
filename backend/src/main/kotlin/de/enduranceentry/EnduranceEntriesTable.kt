package de.enduranceentry

import de.exercise.ExercisesTable
import de.trainingday.TrainingDaysTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object EnduranceEntriesTable : Table("public.endurance_entries") {

    val enduranceEntryId = long("endurance_entry_id").autoIncrement()

    val trainingDayId = reference(
        "training_day_id",
        TrainingDaysTable.trainingDayId
    )

    val exerciseId = reference(
        "exercise_id",
        ExercisesTable.exerciseId
    )

    val durationSec = integer("duration_sec")

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(enduranceEntryId)
}

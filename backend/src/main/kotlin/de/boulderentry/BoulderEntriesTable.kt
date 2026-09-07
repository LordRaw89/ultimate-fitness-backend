package de.boulderentry

import de.trainingday.TrainingDaysTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object BoulderEntriesTable : Table("public.boulder_entries") {

    val boulderEntryId = long("boulder_entry_id")

    val trainingDayId = reference(
        "training_day_id",
        TrainingDaysTable.trainingDayId
    )

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(boulderEntryId)
}

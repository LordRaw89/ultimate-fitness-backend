package de.strengthentry

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object StrengthRepSetsTable : Table("public.strength_rep_sets") {

    val strengthRepSetId = long("strength_rep_set_id")

    val strengthEntryId = reference(
        "strength_entry_id",
        StrengthEntriesTable.strengthEntryId
    )

    val setNumber = short("set_number")

    val reps = short("reps")

    val weightKg = decimal("weight_kg", precision = 8, scale = 2).nullable()

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(strengthRepSetId)
}

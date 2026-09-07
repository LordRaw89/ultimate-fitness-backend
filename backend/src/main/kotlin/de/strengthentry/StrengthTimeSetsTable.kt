package de.strengthentry

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object StrengthTimeSetsTable : Table("public.strength_time_sets") {

    val strengthTimeSetId = long("strength_time_set_id")

    val strengthEntryId = reference(
        "strength_entry_id",
        StrengthEntriesTable.strengthEntryId
    )

    val setNumber = short("set_number")

    val durationSec = integer("duration_sec")

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(strengthTimeSetId)
}

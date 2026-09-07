package de.boulderentry

import de.difficultygrade.DifficultyGradesTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object BoulderRoutesTable : Table("public.boulder_routes") {

    val boulderRouteId = long("boulder_route_id")

    val boulderEntryId = reference(
        "boulder_entry_id",
        BoulderEntriesTable.boulderEntryId
    )

    val difficultyGradeId = reference(
        "difficulty_grade_id",
        DifficultyGradesTable.difficultyGradeId
    ).nullable()

    val routeName = text("route_name").nullable()

    val attempts = short("attempts")

    val result = text("result")

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(boulderRouteId)
}

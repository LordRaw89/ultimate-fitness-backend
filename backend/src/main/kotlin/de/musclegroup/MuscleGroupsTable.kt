package de.musclegroup

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object MuscleGroupsTable : Table("public.muscle_groups") {

    val muscleGroupId = long("muscle_group_id")

    val muscleGroupName = text("muscle_group_name")

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(muscleGroupId)
}

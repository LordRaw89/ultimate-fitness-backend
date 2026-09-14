package de.trainingday

import org.jetbrains.exposed.v1.core.Table
import de.profile.ProfilesTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object TrainingDaysTable : Table("public.training_days") {

    val trainingDayId = long("training_day_id").autoIncrement()

    val profileId = reference(
        "profile_id",
        ProfilesTable.profileId
    )

    val trainingDate = date("training_date")

    val notes = text("notes").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(trainingDayId)
}

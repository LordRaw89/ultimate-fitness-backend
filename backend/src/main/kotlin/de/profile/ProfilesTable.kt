package de.profile

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object ProfilesTable : Table("public.profiles") {

    val profileId = javaUUID("profile_id")

    val username = text("username").uniqueIndex()

    val displayName = text("display_name").nullable()

    val birthdate = date("birthdate").nullable()

    val weightKg = decimal("weight_kg", precision = 5, scale = 2).nullable()

    val heightCm = short("height_cm").nullable()

    val gender = text("gender").nullable()

    val createdAt = timestampWithTimeZone("created_at")

    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(profileId)
}
package de.profile

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID
import java.time.OffsetDateTime

data class Profile(
    val profileId: UUID,
    val username: String,
    val updatedAt: OffsetDateTime
)

object ProfileRepository {

    fun createProfile(
        profileId: UUID,
        username: String
    ) {
        transaction {
            ProfilesTable.insert {
                it[ProfilesTable.profileId] = profileId
                it[ProfilesTable.username] = username
            }
        }
    }

    fun findById(profileId: UUID): Profile? {
        return transaction {
            ProfilesTable
                .selectAll()
                .where { ProfilesTable.profileId eq profileId }
                .singleOrNull()
                ?.let { row ->
                    Profile(
                        profileId = row[ProfilesTable.profileId],
                        username = row[ProfilesTable.username],
                        updatedAt = row[ProfilesTable.updatedAt]
                    )
                }
        }
    }

    fun deleteProfile(profileId: UUID) {
        transaction {
            ProfilesTable.deleteWhere {
                ProfilesTable.profileId eq profileId
            }
        }
    }

    fun updateUsername(
        profileId: UUID,
        username: String
    ) {
        transaction {
            ProfilesTable.update(
                where = { ProfilesTable.profileId eq profileId }
            ) {
                it[ProfilesTable.username] = username
                it[ProfilesTable.updatedAt] = OffsetDateTime.now()
            }
        }
    }
}

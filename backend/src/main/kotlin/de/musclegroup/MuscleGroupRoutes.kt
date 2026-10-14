package de.musclegroup

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.muscleGroupRoutes() {
    route("/muscle-groups") {

        get {
            val muscleGroups = MuscleGroupService
                .getAllMuscleGroups()
                .map { it.toResponse() }

            call.respond(muscleGroups)
        }

        get("/{id}") {
            val muscleGroupId = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid muscle group ID"
                )

            val muscleGroup = MuscleGroupService
                .getMuscleGroupById(muscleGroupId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Muscle group not found"
                )

            call.respond(muscleGroup.toResponse())
        }
    }
}

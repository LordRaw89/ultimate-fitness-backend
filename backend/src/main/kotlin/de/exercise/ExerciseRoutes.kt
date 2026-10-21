package de.exercise

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.exerciseRoutes() {
    route("/exercises") {

        // Retrieve all exercises without muscle groups
        get {
            val exercises = ExerciseService
                .getAllExercises()
                .map { it.toResponse() }

            call.respond(exercises)
        }

        // Retrieve a single exercise by ID
        get("/{id}") {
            val exerciseId = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid exercise ID"
                )

            val exercise = ExerciseService
                .getExerciseById(exerciseId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Exercise not found"
                )

            call.respond(exercise.toResponse())
        }

        // Retrieve a single exercise with its muscle groups
        get("/{id}/with-muscle-groups") {
            val exerciseId = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid exercise ID"
                )

            val exercise = ExerciseService
                .getExerciseWithMuscleGroupsById(exerciseId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Exercise not found"
                )

            call.respond(exercise.toResponse())
        }

        // Retrieve multiple exercises with their muscle groups
        get("/batch-with-muscle-groups") {

            val rawIds = call.request.queryParameters.getAll("ids")

            if (rawIds.isNullOrEmpty()) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "At least one exercise ID is required"
                )
            }

            val exerciseIds = rawIds.map { it.toLongOrNull() }

            if (exerciseIds.any { it == null || it <= 0L }) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "All exercise IDs must be positive numbers"
                )
            }

            val exercises = ExerciseService
                .getExercisesWithMuscleGroupsByIds(
                    exerciseIds.filterNotNull()
                )
                .map { it.toResponse() }

            call.respond(exercises)
        }
    }
}

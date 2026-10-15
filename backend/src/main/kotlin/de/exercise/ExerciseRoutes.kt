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
    }
}

package de.exercisecategory

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.exerciseCategoryRoutes() {
    route("/exercise-categories") {

        get {
            val categories = ExerciseCategoryService
                .getAllCategories()
                .map { it.toResponse() }

            call.respond(categories)
        }

        get("/{id}") {
            val exerciseCategoryId = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid exercise category ID"
                )

            val category = ExerciseCategoryService
                .getCategoryById(exerciseCategoryId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Exercise category not found"
                )

            call.respond(category.toResponse())
        }
    }
}

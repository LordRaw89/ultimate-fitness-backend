package de.difficultygrade

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.difficultyGradeRoutes() {
    route("/difficulty-grades") {

        get {
            val difficultyGrades = DifficultyGradeService
                .getAllDifficultyGrades()
                .map { it.toResponse() }

            call.respond(difficultyGrades)
        }

        get("/{id}") {
            val difficultyGradeId = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid difficulty grade ID"
                )

            val difficultyGrade = DifficultyGradeService
                .getDifficultyGradeById(difficultyGradeId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Difficulty grade not found"
                )

            call.respond(difficultyGrade.toResponse())
        }
    }
}

package de.difficultygrade

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
    }
}

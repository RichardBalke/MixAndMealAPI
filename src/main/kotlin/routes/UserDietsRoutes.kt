package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserDietsService
import models.dto.UserDietEntry
import io.ktor.server.routing.Route
import io.ktor.http.*

fun Route.userDietsRoutes(userDietsService: UserDietsService) {

    route("/diets") {

        get("/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val diets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
            call.respond(diets)
        }

        post("/{userId}/diet") {
            val userId = call.parameters["userId"] ?: return@post call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val dietId = call.receive<Map<String, Int>>()["dietId"]
                ?: return@post call.respondText("Missing dietId", status = HttpStatusCode.BadRequest)

            try {
                val entry = userDietsService.addUserDietEntry(
                    UserDietEntry(userId = userId, dietId = dietId)
                )
                call.respond(HttpStatusCode.Created, entry)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Diet already exists")
            }
        }

        delete("/{userId}/diet") {
            val userId = call.parameters["userId"] ?: return@delete call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val dietId = call.receive<Map<String, Int>>()["dietId"]
                ?: return@delete call.respondText("Missing dietId", status = HttpStatusCode.BadRequest)

            userDietsService.removeUserDietEntry(userId, dietId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
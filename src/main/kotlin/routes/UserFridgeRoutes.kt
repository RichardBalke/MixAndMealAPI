package api.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserFridgeService
import api.models.UserFridgeEntry
import io.ktor.server.routing.Route
import io.ktor.http.*

fun Route.userFridgeRoutes(userFridgeService: UserFridgeService) {

    route("/fridge") {

        get("/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)
            call.respond(fridge)
        }

        post("/{userId}/ingredient") {
            val userId = call.parameters["userId"] ?: return@post call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val ingredient = call.receive<Map<String, String>>()["ingredientName"]
                ?: return@post call.respondText("Missing ingredientName", status = HttpStatusCode.BadRequest)

            try {
                val entry = userFridgeService.addUserFridgeEntry(
                    UserFridgeEntry(userId = userId, ingredientName = ingredient)
                )
                call.respond(HttpStatusCode.Created, entry)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Ingredient already exists")
            }
        }

        delete("/{userId}/ingredient") {
            val userId = call.parameters["userId"] ?: return@delete call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val ingredient = call.receive<Map<String, String>>()["ingredientName"]
                ?: return@delete call.respondText("Missing ingredientName", status = HttpStatusCode.BadRequest)

            userFridgeService.removeUserFridgeEntry(userId, ingredient)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
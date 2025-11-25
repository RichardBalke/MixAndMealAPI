package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserFavouritesService
import api.models.UserFavouritesEntry
import io.ktor.server.routing.Route
import io.ktor.http.*

fun Route.userFavouritesRoutes(userFavouritesService: UserFavouritesService) {

    route("/favourites") {

        get("/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val favourites: List<UserFavouritesEntry> = userFavouritesService.getUserFavouritesEntries(userId)
            call.respond(favourites)
        }

        post("/{userId}/recipe") {
            val userId = call.parameters["userId"] ?: return@post call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val recipeId = call.receive<Map<String, Int>>()["recipeId"]
                ?: return@post call.respondText("Missing recipeId", status = HttpStatusCode.BadRequest)

            try {
                val entry = userFavouritesService.addUserFavouritesEntry(
                    UserFavouritesEntry(userId = userId, recipeId = recipeId)
                )
                call.respond(HttpStatusCode.Created, entry)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Favourite already exists")
            }
        }

        delete("/{userId}/recipe") {
            val userId = call.parameters["userId"] ?: return@delete call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val recipeId = call.receive<Map<String, Int>>()["recipeId"]
                ?: return@delete call.respondText("Missing recipeId", status = HttpStatusCode.BadRequest)

            userFavouritesService.removeUserFavouritesEntry(userId, recipeId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
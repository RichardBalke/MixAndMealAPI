package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserAllergensService
import models.dto.UserAllergenEntry
import io.ktor.http.*

fun Route.userAllergensRoutes(userAllergensService: UserAllergensService) {

    route("/allergens") {

        // GET /allergens/{userId} - haal alle allergens voor een user
        get("/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )

            val allergens = userAllergensService.getUserAllergenEntries(userId)
            call.respond(allergens)
        }

        // POST /allergens/{userId}/allergen - voeg een allergen toe
        post("/{userId}/allergen") {
            val userId = call.parameters["userId"] ?: return@post call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val allergenId = call.receive<Map<String, Int>>()["allergenId"]
                ?: return@post call.respondText("Missing allergenId", status = HttpStatusCode.BadRequest)

            try {
                val entry = userAllergensService.addUserAllergenEntry(userId, allergenId)
                call.respond(HttpStatusCode.Created, entry)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Allergen already exists")
            }
        }

        // DELETE /allergens/{userId}/allergen - verwijder een allergen
        delete("/{userId}/allergen") {
            val userId = call.parameters["userId"] ?: return@delete call.respondText(
                "Missing userId", status = HttpStatusCode.BadRequest
            )
            val allergenId = call.receive<Map<String, Int>>()["allergenId"]
                ?: return@delete call.respondText("Missing allergenId", status = HttpStatusCode.BadRequest)

            userAllergensService.removeUserAllergenEntry(userId, allergenId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}

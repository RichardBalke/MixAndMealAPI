package api.routes

import api.requests.IngredientIDRequest
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserFridgeService
import models.dto.UserFridgeEntry
import io.ktor.server.routing.Route
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.koin.ktor.ext.inject

fun Route.userFridgeRoutes() {
    val userFridgeService by inject<UserFridgeService>()
    authenticate {
        route("/fridge") {
            get() {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)

                call.respond(HttpStatusCode.OK ,fridge)
            }

            post("/ingredient") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val ingredient = call.receive<IngredientIDRequest>()
                try {
                    val entry = userFridgeService.addUserFridgeEntry(
                        UserFridgeEntry(userId = userId, ingredientName = ingredient.ingredientName)
                    )
                    call.respond(HttpStatusCode.Created, entry)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.Conflict, e.message ?: "Ingredient already exists")
                }
            }

            delete("/ingredient") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val ingredient = call.receive<IngredientIDRequest>()

                userFridgeService.removeUserFridgeEntry(userId, ingredient.ingredientName)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

}
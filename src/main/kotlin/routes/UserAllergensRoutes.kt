package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserAllergensService
import models.dto.UserAllergenEntry
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.koin.ktor.ext.inject

fun Route.userAllergensRoutes() {
    val userAllergensService by inject<UserAllergensService>()

    authenticate {
        route("/allergens") {

            // GET /allergens
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val allergens = userAllergensService.getUserAllergenEntries(userId)
                call.respond(HttpStatusCode.OK, allergens)
            }

            // POST /allergens/add-remove-allergen
            post("/add-remove-allergen") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val allergenId = call.receive<Map<String, Int>>()["allergenId"]
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing allergenId"
                    )

                val existingAllergens =
                    userAllergensService.getUserAllergenEntries(userId)
                        .map { it.allergenId }

                if (!existingAllergens.contains(allergenId)) {
                    val entry = userAllergensService.addUserAllergenEntry(userId, allergenId)
                    call.respond(HttpStatusCode.Created, entry)
                } else {
                    userAllergensService.removeUserAllergenEntry(userId, allergenId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

package routes

import api.requests.AllergenIDRequest
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

            // POST /allergens/add-allergen
            post("/add-allergen") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val request = call.receive<AllergenIDRequest>()

                try {
                    userAllergensService.addUserAllergenEntry(userId, request.allergenId)
                    val allergens = userAllergensService.getUserAllergenEntries(userId)
                    call.respond(HttpStatusCode.Created, allergens)
                } catch (e: Exception) {
                    val allergens = userAllergensService.getUserAllergenEntries(userId)
                    call.respond(HttpStatusCode.Conflict, allergens)
                }
            }

            // DELETE /allergens/remove-allergen
            delete("/remove-allergen") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val request = call.receive<AllergenIDRequest>()

                userAllergensService.removeUserAllergenEntry(userId, request.allergenId)

                val allergens = userAllergensService.getUserAllergenEntries(userId)
                call.respond(HttpStatusCode.OK, allergens)
            }
        }
    }
}

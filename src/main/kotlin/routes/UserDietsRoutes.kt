package routes

import api.responses.RecipeCardResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserDietsService
import models.dto.UserDietEntry
import io.ktor.server.routing.Route
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import models.dto.DietEntry
import org.koin.ktor.ext.inject
import service.DietsService

fun Route.userDietsRoutes() {
    val userDietsService by inject<UserDietsService>()
    val dietsService by inject<DietsService>()

    authenticate {
        route("/user-diets") {
            get() {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!
                val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                val diets = mutableListOf<DietEntry>()

                if (userDiets.isNotEmpty()) {
                    diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                }

                call.respond(HttpStatusCode.OK, diets)
            }

            post("/add-diet") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val dietId = call.receive<DietEntry>()

                try {
                    val entry = userDietsService.addUserDietEntry(
                        UserDietEntry(userId = userId, dietId = dietId.id)
                    )
                    call.respond(HttpStatusCode.Created, entry)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.Conflict, e.message ?: "Diet already exists")
                }
            }

            delete("/remove-diet") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val dietId = call.receive<DietEntry>()

                userDietsService.removeUserDietEntry(userId, dietId.id)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
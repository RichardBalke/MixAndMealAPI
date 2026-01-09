package api.routes

import api.repository.UserRepositoryImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import models.dto.UserFridgeEntry
import org.koin.ktor.ext.inject
import service.RecipeImagesService
import service.RecipeService
import service.UserAllergensService
import service.UserFavouritesService
import service.UserFridgeService
import service.UserService
import service.authenticatedUserId
import service.requireAdmin
import kotlin.getValue


fun Route.userRoutes() {
    val userService by inject<UserService>()

    // authenticate zorgt ervoor dat alleen ingelogde users de routes kunnen gebruiken.
    authenticate {

        route("/users") {

            get {
                // Deze if else statement check of de ingelogde user een admin rol heeft
                if (call.requireAdmin()) {
                    val users = userService.getAll()
                    call.respond(users)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }

            }

            get("/{id}") {
                // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                val id: String = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                if (id == call.authenticatedUserId()) {
                    val user = userService.getByEmail(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else if (call.requireAdmin()) {
                    val user = userService.getByEmail(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

        }
    }
}
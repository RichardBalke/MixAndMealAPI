package api.routes

import api.models.Allergens
import api.models.Ingredients
import api.models.Recipes
import api.models.User
import api.repository.FakeUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import service.authenticatedUserId
import service.requireAdmin


fun Route.userRoutes() {
    // authenticate zorgt ervoor dat alleen ingelogde users de routes kunnen gebruiken.
    authenticate {
        route("/users") {

            get {
                // Deze if else statement check of de ingelogde user een admin rol heeft
                if (call.requireAdmin()) {
                    val users = FakeUserRepository.findAll()
                    call.respond(users)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }

            }

            get("/{id}") {
                // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                val id: Long = call.parameters["id"]?.toLongOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                if (id == call.authenticatedUserId()) {
                    val user = FakeUserRepository.findById(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else if (call.requireAdmin()) {
                    val user = FakeUserRepository.findById(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            post("/favourites"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@post call.respond(HttpStatusCode.NotFound, "user not found")

                val request = call.receiveNullable<Recipes>() ?: return@post call.respond(HttpStatusCode.BadRequest)

                if(user.favourites.any{it.id == request.id }) {
                    user.favourites.remove(request)
                    call.respond(HttpStatusCode.Accepted, user.favourites)
                }
                else{
                    user.favourites.add(request)
                    call.respond(HttpStatusCode.Accepted, user.favourites)
                }
            }

            get("/favourites") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLong()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "user not found")

                call.respond(HttpStatusCode.OK, user.favourites)
            }

            post("/fridge"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@post call.respond(HttpStatusCode.NotFound, "user not found")

                val request = call.receiveNullable<Ingredients>() ?: return@post call.respond(HttpStatusCode.BadRequest)

                if(user.fridge.any{it.id == request.id }) {
                    user.fridge.remove(request)
                    call.respond(HttpStatusCode.Accepted, user.fridge)
                }
                else{
                    user.fridge.add(request)
                    call.respond(HttpStatusCode.Accepted, user.fridge)
                }
            }

            get("/fridge") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLong()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "user not found")

                call.respond(HttpStatusCode.OK, user.fridge)
            }

            post("/allergens"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.Forbidden, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@post call.respond(HttpStatusCode.NotFound, "user not found")

                val request = call.receiveNullable<Allergens>() ?: return@post call.respond(HttpStatusCode.Forbidden, "Allergen is not correct")

                if(user.allergens.any{it.id == request.id }) {
                    user.allergens.remove(request)
                    call.respond(HttpStatusCode.Accepted, user.allergens)
                }
                else{
                    user.allergens.add(request)
                    call.respond(HttpStatusCode.Accepted, user.allergens)
                }
            }

            get("/allergens") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLong()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = FakeUserRepository.findById(userId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "user not found")

                call.respond(HttpStatusCode.OK, user.allergens)
            }

            post {
                val newUser = call.receive<User>()
                val created = FakeUserRepository.create(newUser)
                call.respond(HttpStatusCode.Created, created)
            }

        }
    }
}
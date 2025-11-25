package api.routes

import api.models.Allergen
import api.models.Ingredient
import api.repository.IngredientsRepositoryImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.ingredientsRoutes() {
    val ingredientrepo =  IngredientsRepositoryImpl()
    route("/ingredients") {

        // GET all ingredients
        get {
            call.respond(ingredientrepo.findAll())
        }


        // GET by ID
        get("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }
            val ingredient = ingredientrepo.findById(id)
            if (ingredient == null) {
                call.respond(HttpStatusCode.NotFound, "Ingredient not found")
            } else {
                call.respond(ingredient)
            }
        }

        // GET by name
        get("/name/{name}") {
            val name = call.parameters["name"]
            val ingredient = name?.let { ingredientrepo.findByName(it) }
            if (ingredient == null) {
                call.respond(HttpStatusCode.NotFound, "Ingredient not found")
            } else {
                call.respond(ingredient)
            }
        }

        authenticate {
            // POST create
            post {
                val request = call.receive<Ingredient>()
                val created = ingredientrepo.create(request)
                call.respond(HttpStatusCode.Created, created)
            }

            // PUT update full ingredient
//            put {
//                val request = call.receive<Ingredients>()
//                FakeIngredientsRepository.update(request)
//                call.respond(HttpStatusCode.OK, "Ingredient updated")
//            }

            // PATCH update allergens
            patch("/{id}/allergens") {
                val id = call.parameters["id"]
                val allergens = call.receive<List<Allergen>>()
                val newAllergens : List<Int> = allergens.map { it.id }
                val ingredient = id?.let { ingredientrepo.findById(it) }

                if (ingredient == null) {
                    call.respond(HttpStatusCode.NotFound, "Ingredient not found")
                    return@patch
                }

                val updated = ingredientrepo.updateAllergens(ingredient.name, newAllergens)
                call.respond(updated)
            }

            // DELETE ingredient
            delete("/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }
                val success = ingredientrepo.delete(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Ingredient deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Ingredient not found")
                }
            }
        }
    }
}
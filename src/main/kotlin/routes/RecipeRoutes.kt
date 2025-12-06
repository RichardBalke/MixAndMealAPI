package api.routes

import api.repository.IngredientUnitRepositoryImpl
import api.repository.RecipesRepositoryImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import models.dto.IngredientEntry
import models.dto.RecipeEntry
import responses.FullRecipeScreenResponse
import service.AllergenService
import service.DietsService
import service.IngredientUnitService
import service.RecipeAllergenService
import service.RecipeDietsService
import service.RecipeService
import service.requireAdmin

fun Route.getFullRecipe(
    recipeService: RecipeService,
    recipeDietService : RecipeDietsService,
    dietsService: DietsService,
    recipeAllergenService : RecipeAllergenService,
    allergenService: AllergenService,
    ingredientUnitService: IngredientUnitService
) {
    route("/fullrecipe") {
        get("/{recipeId}"){
            val id = call.parameters["recipeId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)

            val recipe = recipeService.getRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            val diets = recipeDietService.getDietsbyRecipeId(id, dietsService)
            val allergens = recipeAllergenService.getAllergensByRecipeId(id, allergenService)
            val ingredients = ingredientUnitService.getIngredientsByRecipeId(id)

            val fullRecipe = FullRecipeScreenResponse(
                recipe.id,
                recipe.title,
                recipe.description,
                recipe.prepTime,
                recipe.cookingTime,
                recipe.difficulty,
                recipe.image,
                recipe.mealType,
                recipe.kitchenStyle,
                diets,
                allergens,
                ingredients
            )
            call.respond(HttpStatusCode.OK, fullRecipe)
        }
    }
}

fun Route.recipesRoutes(recipeRepo : RecipeService) {

    route("/recipes") {

        // Get all recipes
        get {
            call.respond(recipeRepo.getAllRecipes())
        }
//        authenticate {
            post {
                if(call.requireAdmin()){
                    val request = call.receive<RecipeEntry>()
                    val created = recipeRepo.addRecipes(request)
                    call.respond(HttpStatusCode.Created, created)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }

            }
        }

        //Get recipes by title
        get("/title/{title}") {
            val name = call.parameters["title"].toString()
            val title = recipeRepo.findByTitle(name)
            if (title == null) {
                call.respond(HttpStatusCode.NotFound, "Recipe not found.")
            } else {
                call.respond(title)
            }
        }

        //Get by mealtype
//        get("/mealtype/{mealtype}") {
//            val type = call.parameters["mealtype"]?.lowercase()
//
//            if (type == null) {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//
//            val recipes = recipeRepo.findByMealType(type)
//
//            if (recipes.isEmpty()) {
//                call.respond(HttpStatusCode.NotFound, "No recipes found for meal type $type.")
//            } else {
//                call.respond(recipes)
//            }
//        }

        //Get by difficulty
//        get("/difficulty/{difficulty}") {
//            val diff = call.parameters["difficulty"]?.lowercase()
//
//            if (diff == null) {
//                call.respond(HttpStatusCode.BadRequest)
//            }
//
//            val difficulty = recipeRepo.findByDifficulty(diff)
//
//            if (difficulty.isEmpty()) {
//                call.respond(HttpStatusCode.BadRequest, "Invalid difficulty level '$difficulty'.")
//            } else {
//                call.respond(difficulty)
//            }
//        }

        //Get by diets
//        get("/diets/{diet}") {
//            fun String?.isValidDietDisplayName(): Boolean {
//                if (this == null) return false
//                return Diet.entries.any { it.displayName.lowercase() == this.lowercase() }
//            }
//
//            val diets = call.parameters["diet"]?.lowercase()
//
//            if (diets == null || !diets.isValidDietDisplayName()) {
//                call.respond(HttpStatusCode.BadRequest, "Invalid diet choice '$diets")
//                return@get
//            }
//
//            val dietsChoice = recipeRepo.findByDiets(diets)
//
//            if (dietsChoice.isEmpty()) {
//                call.respond(HttpStatusCode.BadRequest, "Invalid diet choice '$diets'")
//            } else {
//                call.respond(dietsChoice)
//            }
//        }

//        Get by kitchen style
//        get("/kitchen/{style}") {
//            val style = call.parameters["style"]?.lowercase()
//
//            if (style == null) {
//                call.respond(HttpStatusCode.BadRequest, "Missing kitchen style.")
//                return@get
//            }
//
//            val recipes = recipeRepo.findByKitchenStyle(style)
//
//            if (recipes.isEmpty()) {
//                call.respond(HttpStatusCode.NotFound, "No recipes found for kitchen style '$style'.")
//            } else {
//                call.respond(recipes)
//            }
//
//
//        }

        get("/{id}") {
            // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
            val id: Int = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            // controleert of de user met 'id' bestaat
//                val recipe = FakeRecipeRepository.recipeService.findById(id)
            val recipe = recipeRepo.getRecipe(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(HttpStatusCode.OK, recipe)
        }

        authenticate {
            delete("/{id}") {
                if(call.requireAdmin()){
                    // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                    val id: Int = call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    val succes = recipeRepo.deleteRecipe(id)
                    if (succes) {
                        call.respond(HttpStatusCode.OK, "Recipe with id: $id succesfully deleted.")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Recipe with id: $id not found.")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

//            put("/{id}") {
//                if(call.requireAdmin()){
//                    val id = call.parameters["id"]?.toLongOrNull()
//                        ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
//
//                    val request = try {
//                        call.receive<Recipes>()
//                    } catch (e: Exception) {
//                        return@put call.respond(
//                            HttpStatusCode.BadRequest,
//                            "Invalid request body: ${e.message ?: "malformed JSON"}"
//                        )
//                    }
//
//                    val updatedRecipe = request.copy(id = id)
//
//                    try {
//                        FakeRecipeRepository.update(updatedRecipe)
//                        call.respond(HttpStatusCode.OK, updatedRecipe)
//                    } catch (e: IllegalArgumentException) {
//                        call.respond(HttpStatusCode.NotFound, e.message ?: "Recipe not found")
//                    } catch (e: Exception) {
//                        call.respond(HttpStatusCode.InternalServerError, "Update failed: ${e.message}")
//                    }
//                } else {
//                    call.respond(HttpStatusCode.Unauthorized)
//                }
//
//            }

        }
        post("/ingredient"){
            val ingredient = call.receive<IngredientEntry>()
            val ingredientRepo = IngredientUnitRepositoryImpl()
            val foundRecipes = ingredientRepo.findRecipesByIngredient(ingredient.name)

            if(foundRecipes.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, foundRecipes)
            }
            else{
                call.respond(HttpStatusCode.NotFound)
            }

//        }
    }
}
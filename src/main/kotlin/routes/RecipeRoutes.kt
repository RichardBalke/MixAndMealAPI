package api.routes

import api.repository.IngredientUnitRepositoryImpl
import api.repository.RecipesRepositoryImpl
import api.responses.RecipeCardResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import jdk.internal.vm.ScopedValueContainer.call
import models.dto.IngredientEntry
import models.dto.RecipeEntry
import org.koin.ktor.ext.inject
import requests.RecipeSearchRequest
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

            val recipe = recipeService.getRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Recipe not found")
            val diets = recipeDietService.getDietsbyRecipeId(id, dietsService)
            val allergens = recipeAllergenService.getAllergensByRecipeId(id, allergenService)
            val ingredients = ingredientUnitService.getIngredientsByRecipeId(id)

            val fullRecipe = FullRecipeScreenResponse(
                recipe.id,
                recipe.title,
                recipe.description,
                recipe.instructions,
                recipe.prepTime,
                recipe.cookingTime,
                recipe.difficulty,
                listOf("https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"),
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

//fun Route.recipeSearchResults(recipeService: RecipeService){
//    route("recipe/search"){
//        post(){
//            val request = call.receive<RecipeSearchRequest>()
//            val recipes = recipeService.searchRecipes(request)
//
//            if(recipes.isEmpty()){
//                call.respond(HttpStatusCode.NoContent, "No recipes found")
//            }
//            else{
//                call.respond(HttpStatusCode.OK, recipes)
//            }
//
//        }
//    }
//}

fun Route.featuredRecipeDetails(){
    route("/recipes/featured"){
        get("/{recipeId}") {
            val id = call.parameters["recipeId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val recipeService = RecipeService()
            val recipe = recipeService.getRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Recipe not found")

            val response : RecipeCardResponse = RecipeCardResponse(
                recipe.id,
                recipe.title,
                recipe.description,
                recipe.cookingTime,
                listOf("https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg")
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun Route.recipesRoutes(recipeService: RecipeService) {

    route("/recipes") {

        // Get all recipes
        get {
            val recipeRepo = RecipesRepositoryImpl()
            call.respond(recipeRepo.findAll())
        }

            post {
                if(call.requireAdmin()){
                    val request = call.receive<RecipeEntry>()
                    val created = recipeService.addRecipes(request)
                    call.respond(HttpStatusCode.Created, created)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }

            }
        }

        //Get recipes by title
        get("/title/{title}") {
            val recipeRepo = RecipesRepositoryImpl()
            val name = call.parameters["title"].toString()
            val title = recipeRepo.findByTitle(name)
            if (title == null) {
                call.respond(HttpStatusCode.NotFound, "Recipe not found.")
            } else {
                call.respond(title)
            }
        }

        get("/{id}") {
            // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
            val id: Int = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            // controleert of de user met 'id' bestaat
//                val recipe = FakeRecipeRepository.recipeService.findById(id)
            val recipe = recipeService.getRecipe(id)
                ?: return@get call.respond(HttpStatusCode.BadGateway, "Recipe not found")

            call.respond(HttpStatusCode.OK, recipe)
        }

        authenticate {
            delete("/{id}") {
                if(call.requireAdmin()){
                    // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                    val id: Int = call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    val succes = recipeService.deleteRecipe(id)
                    if (succes) {
                        call.respond(HttpStatusCode.OK, "Recipe with id: $id succesfully deleted.")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Recipe with id: $id not found.")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

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

    }
}
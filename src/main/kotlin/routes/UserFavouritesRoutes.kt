package routes

import api.requests.RecipeIDRequest
import api.responses.RecipeCardResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserFavouritesService
import models.dto.UserFavouritesEntry
import io.ktor.server.routing.Route
import io.ktor.http.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import service.RecipeImagesService
import service.RecipeService

fun Route.userFavouritesRoutes() {
    val recipeService by inject<RecipeService>()
    val userFavouritesService by inject<UserFavouritesService>()
    val recipeImagesService by inject<RecipeImagesService>()

    route("/favourites") {
        get(){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)!!
            val favourites: List<UserFavouritesEntry> = userFavouritesService.getUserFavouritesEntries(userId)
            val recipes = mutableListOf<RecipeCardResponse>()

            if (favourites.isNotEmpty()) {
                recipes.addAll(recipeService.findFavouriteRecipes(favourites, recipeImagesService))
            }

            call.respond(HttpStatusCode.OK,recipes)
        }


        post("/add-remove-favourite-recipe") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)!!
            val recipeId = call.receive<RecipeIDRequest>().recipeId

            val isFavourite = userFavouritesService.checkFavouriteExists(userId, recipeId)

            if(isFavourite) {
                val entry = userFavouritesService.addUserFavouritesEntry(
                    UserFavouritesEntry(userId = userId, recipeId = recipeId)
                )
                call.respond(HttpStatusCode.Created, entry)
            }
            else{
                userFavouritesService.removeUserFavouritesEntry(userId, recipeId)
            }
        }
    }
}
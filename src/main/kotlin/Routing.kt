package api

import api.routes.featuredRecipeDetails
import api.routes.fullRecipe
import api.routes.ingredientsRoutes
import api.routes.popularRecipes
import api.routes.quickRecipes
import api.routes.recipeCardsByDifficulty
import api.routes.userRoutes
import api.routes.recipesRoutes
import api.routes.userFridgeRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import models.dto.TokenConfig
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import routes.userDietsRoutes
import routes.userFavouritesRoutes
import service.JwtService

fun Application.configureRouting(
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        featuredRecipeDetails()
        userRoutes()
        ingredientsRoutes()
        recipesRoutes()
        fullRecipe()
        popularRecipes()
        recipeCardsByDifficulty()
        quickRecipes()
        userFridgeRoutes()
        signUp()
        signIn(tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        userDietsRoutes()
        userFavouritesRoutes()
    }
}

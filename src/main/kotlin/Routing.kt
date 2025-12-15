package api

import api.repository.IngredientUnitRepositoryImpl
import api.repository.RecipesRepositoryImpl
import api.routes.featuredRecipeDetails
import api.routes.getFullRecipe
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import api.routes.recipesRoutes
import api.routes.userFridgeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.dto.TokenConfig
import org.koin.ktor.ext.inject
import repository.AllergensRepositoryImpl
import repository.DietsRepositoryImpl
import repository.RecipeAllergensRepositoryImpl
import repository.RecipeDietsRepositoryImpl
import repository.UserFridgeRepositoryImpl
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import service.AllergenService
import service.DietsService
import service.IngredientUnitService
import service.JwtService
import service.RecipeAllergenService
import service.RecipeDietsService
import service.RecipeService
import service.UserFridgeService
import service.UserService

fun Application.configureRouting(
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        featuredRecipeDetails()
        userRoutes()
        ingredientsRoutes()
        recipesRoutes()
        getFullRecipe()
        userFridgeRoutes(UserFridgeService(UserFridgeRepositoryImpl()))
        signUp()
        signIn(tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

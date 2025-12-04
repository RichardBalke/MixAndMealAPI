package api

import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import api.routes.recipesRoutes
import api.routes.userFridgeRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.dto.TokenConfig
import repository.UserFridgeRepositoryImpl
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import service.JwtService
import service.RecipeService
import service.UserFridgeService

fun Application.configureRouting(
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        userRoutes()
        ingredientsRoutes()
        recipesRoutes(RecipeService(RecipesRepositoryImpl()))

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

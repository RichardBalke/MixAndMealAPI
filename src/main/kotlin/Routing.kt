package api

import api.repository.FakeIngredientsRepository
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import api.routes.recipesRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.TokenConfig
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import service.JwtService
import service.RecipeService
import service.TokenService

fun Application.configureRouting(
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        userRoutes()
        ingredientsRoutes()
        recipesRoutes()

        signUp()
        signIn(tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

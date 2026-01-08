package routes

import api.repository.UserRepositoryImpl
import api.responses.RoleResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.dto.TokenClaim
import models.dto.TokenConfig
import models.dto.UserEntry
import org.koin.ktor.ext.inject
import requests.AuthRequest
import requests.Login
import responses.AuthResponse
import service.JwtService
import service.RecipeService
import service.UserService
import service.requireAdmin
import kotlin.getValue



fun Route.signUp(){
    val userRepo = UserRepositoryImpl()
    post("/signup"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }


        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 2
        if(areFieldsBlank || isPwTooShort){
            call.respond(HttpStatusCode.ExpectationFailed)
            return@post
        }

        if(userRepo.findByEmail(request.email) != null){
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }

        val user = UserEntry(
            name = request.username,
            password = request.password,
            email = request.email,
        )
        userRepo.create(user)

        call.respond(HttpStatusCode.OK)

    }
}

fun Route.signIn(
    tokenService: JwtService,
    tokenConfig : TokenConfig
){
    val userService by inject<UserService>()
    post("/signin"){
        val request = call.receiveNullable<Login>()
        if(request == null){
        call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getByEmail(request.email)
        if(user == null){
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val isValidPassword = request.password == user.password

        if(!isValidPassword){
            call.respond(HttpStatusCode.Unauthorized, "Incorrect email or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.email
            ),
            TokenClaim(name = "userName",
                value = user.name)
        )
        call.respond(status = HttpStatusCode.OK,
            message = AuthResponse(token = token))
    }
}


fun Route.authenticated() {
    authenticate {
        get("/authenticate") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)?: ""
            val userName = principal?.getClaim("userName", String::class)?: ""
            // This logic looks okay, assuming requireAdmin() works correctly
            val isAdmin = call.requireAdmin()

            if (isAdmin) {
                // Returns {"role": "ADMIN"}
                call.respond(HttpStatusCode.OK, RoleResponse("ADMIN", userId, userName))
            } else {
                // Returns {"role": "USER"}
                call.respond(HttpStatusCode.OK, RoleResponse("USER", userId, userName))
            }
        }
    }
}



fun Route.getSecretInfo(){
    authenticate {
        get("/secret"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}
package routes

import api.models.User
import api.repository.FakeUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.TokenClaim
import models.TokenConfig
import requests.AuthRequest
import responses.AuthResponse
import service.JwtService
import service.requireAdmin


fun Route.signUp(){
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

        if(FakeUserRepository.findByEmail(request.email) != null){
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }

        val user = User(
            name = request.username,
            password = request.password,
            email = request.email,
        )
        FakeUserRepository.create(user)

        call.respond(HttpStatusCode.OK)

    }
}

fun Route.signIn(
    tokenService: JwtService,
    tokenConfig : TokenConfig
){
    post("/signin"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = FakeUserRepository.findByUsername(request.username)
        if(user == null){
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val isValidPassword = request.password == user.password

        if(!isValidPassword){
            call.respond(HttpStatusCode.Unauthorized, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        call.respond(status = HttpStatusCode.OK,
            message = AuthResponse(token = token))
    }
}


fun Route.authenticated() {
    authenticate {
        get("/authenticate"){
            if(call.requireAdmin()){
                call.respond(HttpStatusCode.OK, "You are an admin")
            } else {
                call.respond(HttpStatusCode.Unauthorized)
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
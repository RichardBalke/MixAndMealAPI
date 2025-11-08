package api

import io.ktor.server.application.*
import models.TokenConfig
import service.JwtService

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val tokenService = JwtService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )


    configureSerialization()
    configureSecurity(tokenConfig)
    // Database will be implemented in the new version.
//    configureDatabases()
    configureRouting(tokenService, tokenConfig)
}

package api

import api.repository.UserRepositoryImpl
import io.ktor.server.application.*
import models.dto.TokenConfig
import org.jetbrains.exposed.sql.Database
import service.JwtService

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}



fun Application.module() {
    initDatabase()
    val tokenService = JwtService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(tokenService, tokenConfig)
}

fun Application.initDatabase() {
    Database.connect(
//        url = "jdbc:postgresql://docker-db-1",
        url = "jdbc:postgresql://localhost:5432/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "admin"
    )
}

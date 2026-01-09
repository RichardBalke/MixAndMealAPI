package service

import api.models.Role
import api.repository.UserRepositoryImpl
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal


suspend fun ApplicationCall.authenticatedUserId(): String {
    val principal = authentication.principal<JWTPrincipal>()
    val id = principal?.getClaim("userId", String::class)
    if (id != null) {
        return id
    } else {
        return ""
    }
}

suspend fun ApplicationCall.requireAdmin(): Boolean {
    val principal = authentication.principal<JWTPrincipal>()
    // Change "email" to "userId" to match your signIn token generation
    val id = principal?.getClaim("userId", String::class)

    if (id != null) {
        val role = UserRepositoryImpl().getRoleById(id)
        return (role == Role.ADMIN)
    } else {
        return false
    }
}






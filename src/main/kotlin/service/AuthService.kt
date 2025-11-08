package service

import api.models.Role
import api.repository.FakeUserRepository.getRoleById
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal


suspend fun ApplicationCall.authenticatedUserId(): Long {
    val principal = authentication.principal<JWTPrincipal>()
    val id = principal?.getClaim("userId", String::class)?.toLong()
    if (id != null) {
        return id
    } else {
        return -1
    }
}

suspend fun ApplicationCall.requireAdmin(): Boolean {
    val principal = authentication.principal<JWTPrincipal>()
    val id = principal?.getClaim("userId", String::class)?.toLong()
    if (id != null) {
        val role = getRoleById(id)
        return (role == Role.ADMIN)
    } else {
        return false
    }

}

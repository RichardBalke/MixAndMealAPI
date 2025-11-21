package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER

)

object Users: Table() {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val role = varchar("role", 255)
}


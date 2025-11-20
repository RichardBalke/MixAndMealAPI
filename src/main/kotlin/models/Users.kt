package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER

//    val locationSettings: LocationSettings(), wordt toegevoegd bij implementatie app

//    val favourites: MutableList<Recipes> = mutableListOf<Recipes>(),
//    val fridge: MutableList<Ingredients> = mutableListOf<Ingredients>(),
//    val allergens: MutableList<Allergens> = mutableListOf<Allergens>(),
)
// {
//    companion object {
//        const val NEW_USER_ID: Long = 0L
//    }
//}

object Users: Table(){
    val user_id = integer("user_id").uniqueIndex()
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val role = varchar("role", 255)
}


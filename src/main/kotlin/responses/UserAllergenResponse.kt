package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserAllergenResponse (
    val allergenName: String
)
package api.requests

import kotlinx.serialization.Serializable

@Serializable
data class AllergenIDRequest(
    val allergenId: Int
)
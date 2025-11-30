package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDietEntry (
    val userId: String,
    val dietId: Int,
)
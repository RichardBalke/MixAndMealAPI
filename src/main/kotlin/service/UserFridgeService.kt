package service

import api.models.UserFridgeEntry
import repository.UserFridgeRepository

class UserFridgeService(private val userFridgeRepository: UserFridgeRepository) {

    suspend fun getUserFridgeEntries(userId: String): List<UserFridgeEntry> {
        return userFridgeRepository.getFridgeForUser(userId)
    }

    suspend fun addUserFridgeEntry(entry: UserFridgeEntry): UserFridgeEntry {
        return userFridgeRepository.addIngredient(entry.userId, entry.ingredientName)
    }

    suspend fun removeUserFridgeEntry(userId: String, ingredientName: String) {
        userFridgeRepository.removeIngredient(userId, ingredientName)
    }
}
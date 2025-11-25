package service

import repository.UserAllergensRepository
import api.models.UserAllergenEntry

class UserAllergensService(private val userAllergensRepository: UserAllergensRepository) {

    suspend fun getUserAllergenEntries(userId: String): List<UserAllergenEntry> {
        return userAllergensRepository.getAllergensForUser(userId)
    }

    suspend fun addUserAllergenEntry(userId: String, allergenId: Int): UserAllergenEntry {
        return userAllergensRepository.addAllergen(userId, allergenId)
    }

    suspend fun removeUserAllergenEntry(userId: String, allergenId: Int) {
        userAllergensRepository.removeAllergen(userId, allergenId)
    }
}
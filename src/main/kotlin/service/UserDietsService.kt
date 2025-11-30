package service

import models.dto.UserDietEntry
import repository.UserDietsRepository

class UserDietsService(private val userDietsRepository: UserDietsRepository) {

    suspend fun getUserDietEntries(userId: String): List<UserDietEntry> {
        return userDietsRepository.getDietsForUser(userId)
    }

    suspend fun addUserDietEntry(entry: UserDietEntry): UserDietEntry {
        return userDietsRepository.addDiet(entry.userId, entry.dietId)
    }

    suspend fun removeUserDietEntry(userId: String, dietId: Int) {
        userDietsRepository.removeDiet(userId, dietId)
    }
}
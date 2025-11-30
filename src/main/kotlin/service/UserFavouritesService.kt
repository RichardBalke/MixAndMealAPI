package service

import models.dto.UserFavouritesEntry
import repository.UserFavouritesRepository

class UserFavouritesService(private val userFavouritesRepository: UserFavouritesRepository) {

    suspend fun getUserFavouritesEntries(userId: String): List<UserFavouritesEntry> {
        return userFavouritesRepository.getFavouritesForUser(userId)
    }

    suspend fun addUserFavouritesEntry(entry: UserFavouritesEntry): UserFavouritesEntry {
        return userFavouritesRepository.addFavourite(entry.userId, entry.recipeId)
    }

    suspend fun removeUserFavouritesEntry(userId: String, recipeId: Int) {
        userFavouritesRepository.removeFavourite(userId, recipeId)
    }
}
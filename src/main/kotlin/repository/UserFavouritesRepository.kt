package repository

import api.models.UserFavouriteEntry
import api.models.UserFavourites
import api.repository.CrudImplementation
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserFavouritesRepository {
    suspend fun getFavouritesForUser(userId: String): List<UserFavouriteEntry>
    suspend fun addFavourite(userId: String, recipeId: Int): UserFavouriteEntry
    suspend fun removeFavourite(userId: String, recipeId: Int)
}

class UserFavouritesRepositoryImpl :
    UserFavouritesRepository,
    CrudImplementation<UserFavouriteEntry, UserFavouriteEntry>(
        table = UserFavourites,
        toEntity = { row ->
            UserFavouriteEntry(
                userId = row[UserFavourites.userId],
                recipeId = row[UserFavourites.recipeId]
            )
        },
        idColumns = listOf(UserFavourites.userId, UserFavourites.recipeId),
        idExtractor = { entry -> listOf(entry.userId, entry.recipeId) },
        entityMapper = { stmt, entry ->
            stmt[UserFavourites.userId] = entry.userId
            stmt[UserFavourites.recipeId] = entry.recipeId
        }
    ) {

    override suspend fun getFavouritesForUser(userId: String): List<UserFavouriteEntry> = transaction {
        UserFavourites
            .select(UserFavourites.userId eq userId)
            .map(toEntity)
    }

    override suspend fun addFavourite(userId: String, recipeId: Int): UserFavouriteEntry {
        val entry = UserFavouriteEntry(userId, recipeId)
        return create(entry)
    }

    override suspend fun removeFavourite(userId: String, recipeId: Int) {
        delete(UserFavouriteEntry(userId, recipeId))
    }
}
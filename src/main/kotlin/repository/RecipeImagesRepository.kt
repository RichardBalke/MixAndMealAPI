package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.RecipeImageEntry
import models.tables.RecipeImages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction



interface RecipeImageRepository {
    suspend fun getImagesForRecipe(recipeId: Int): List<RecipeImageEntry>
    suspend fun addImage(recipeId: Int, imageUrl: String): RecipeImageEntry
    suspend fun deleteImage(imageId: Int)
}

class RecipeImagesRepositoryImpl :
    RecipeImageRepository,
    CrudImplementation<RecipeImageEntry, RecipeImageEntry>(
        table = RecipeImages,
        toEntity = { row ->
            RecipeImageEntry(
                id = row[RecipeImages.id],
                recipeId = row[RecipeImages.recipeId],
                imageUrl = row[RecipeImages.imageUrl]
            )
        },
        idColumns = listOf(RecipeImages.id),
        idExtractor = { entry -> listOf(entry.id) },
        entityMapper = { stmt, entry ->
            stmt[RecipeImages.recipeId] = entry.recipeId
            stmt[RecipeImages.imageUrl] = entry.imageUrl
        }
    ) {
    override suspend fun getImagesForRecipe(recipeId: Int): List<RecipeImageEntry> = transaction {
        RecipeImages
            .selectAll()
            .where(RecipeImages.id eq recipeId)
            .map(toEntity)
    }

    override suspend fun addImage(recipeId: Int, imageUrl: String): RecipeImageEntry {
        val entry = RecipeImageEntry(id = 0, recipeId = recipeId, imageUrl = imageUrl)
        return create(entry)
    }

    override suspend fun deleteImage(imageId: Int) {
        delete(RecipeImageEntry(id = imageId, recipeId = 0, imageUrl = ""))
    }
}

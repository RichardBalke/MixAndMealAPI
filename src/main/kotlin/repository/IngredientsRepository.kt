package api.repository

import models.dto.IngredientEntry
import models.tables.Ingredients
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface IngredientsRepository {
    suspend fun findByName(name: String): IngredientEntry?
    suspend fun updateAllergens(ingredientName : String, newAllergens : List<Int>) : IngredientEntry
}

class IngredientsRepositoryImpl : IngredientsRepository, CrudImplementation<IngredientEntry, String>(
    table = Ingredients,
    toEntity = { row ->
        IngredientEntry(row[Ingredients.name], row[Ingredients.description]) },
    idColumns = listOf(Ingredients.name),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, ingredient ->
        stmt[Ingredients.name] = ingredient.name
        stmt[Ingredients.description] = ingredient.description
    }
) {
    override suspend fun findByName(name: String): IngredientEntry? = transaction {
        Ingredients.selectAll()
            .where { Ingredients.name like name }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun updateAllergens(
        ingredientName: String,
        newAllergens: List<Int>
    ): IngredientEntry {
        TODO("Not yet implemented")
    }

}

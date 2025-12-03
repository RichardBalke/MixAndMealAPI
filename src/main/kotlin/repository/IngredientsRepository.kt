package api.repository

import models.dto.IngredientEntry
import models.tables.Ingredient
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface IngredientsRepository {
    suspend fun findByName(name: String): IngredientEntry?
    suspend fun updateAllergens(ingredientName : String, newAllergens : List<Int>) : IngredientEntry
}

class IngredientsRepositoryImpl : IngredientsRepository, CrudImplementation<IngredientEntry, String>(
    table = Ingredient,
    toEntity = { row ->
        IngredientEntry(row[Ingredient.name], row[Ingredient.description]) },
    idColumns = listOf(Ingredient.name),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, ingredient ->
        stmt[Ingredient.name] = ingredient.name
        stmt[Ingredient.description] = ingredient.description
    }
) {
    override suspend fun findByName(name: String): IngredientEntry? = transaction {
        Ingredient.selectAll()
            .where { Ingredient.name like name }
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

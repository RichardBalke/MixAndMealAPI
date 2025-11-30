package api.repository

import api.models.Ingredient
import api.models.Ingredients
import api.models.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface IngredientsRepository {
    suspend fun findByName(name: String): Ingredient?
    suspend fun updateAllergens(ingredientName : String, newAllergens : List<Int>) : Ingredient
}

class IngredientsRepositoryImpl : IngredientsRepository, CrudImplementation<Ingredient, String>(
    table = Ingredients,
    toEntity = { row ->
        Ingredient(row[Ingredients.name], row[Ingredients.description]) },
    idColumns = listOf(Ingredients.name),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, ingredient ->
        stmt[Ingredients.name] = ingredient.name
        stmt[Ingredients.description] = ingredient.description
    }
) {
    override suspend fun findByName(name: String): Ingredient? = transaction {
        Ingredients.selectAll()
            .where { Ingredients.name like name }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun updateAllergens(
        ingredientName: String,
        newAllergens: List<Int>
    ): Ingredient {
        TODO("Not yet implemented")
    }

}

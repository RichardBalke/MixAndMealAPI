package api.repository

import api.models.Ingredient
import api.models.Ingredients

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
    override suspend fun findByName(name: String): Ingredient? {
        TODO("Not yet implemented")
    }

    override suspend fun updateAllergens(
        ingredientName: String,
        newAllergens: List<Int>
    ): Ingredient {
        TODO("Not yet implemented")
    }

}



//object FakeIngredientsRepository : IngredientsRepository {
//    public var currentId : Long = 0
//    public val ingredients : MutableList<Ingredients> = mutableListOf()
//
//    init{
//        runBlocking {
//            create(Ingredients("Apple", "A red apple"))
//            create(Ingredients("Sugar", "A sweet substance"))
//            create(Ingredients("Peanut", "A tasty nut", listOf(Allergens(5, "PEANUTS", "Pinda’s", "Kan ernstige allergische reacties veroorzaken"))))
//            create(Ingredients("Lentils", "Hearty and protein-rich lentils"))
//            create(Ingredients("Tomatoes", "Fresh ripe tomatoes"))
//            create(Ingredients("Spices", "A mix of Indian spices"))
//        }
//    }
//    override suspend fun create(entity: Ingredients): Ingredients {
//        currentId++
//        val newIngredient = entity.copy(id = currentId)
//        ingredients.add(newIngredient)
//        return newIngredient
//    }
//
//    override suspend fun updateAllergens(entity: Ingredients, newAllergens: List<Allergens>): Ingredients {
//        check(entity.id > 0) { "ID must be greater than 0." }
//        val index = ingredients.indexOfFirst { it.id == entity.id }
//        require(index != -1) { "Ingredient with ID ${entity.id} not found." }
//        val updated = entity.copy(allergens = newAllergens)
//        ingredients[index] = updated
//        return updated
//    }
//
//    override suspend fun delete(id: Long): Boolean {
//        return ingredients.removeIf { id == it.id }
//    }
//
//    override suspend fun update(id: Long, entity: Ingredients): Ingredients {
//        check(entity.id > 0) { "ID must be greater than 0." }
//        require(ingredients.any { it.id == entity.id })
//        ingredients.removeIf { entity.id == it.id }
//        ingredients.add(entity)
//        return entity
//    }
//
//    override suspend fun findByName(name: String): Ingredients?{
//        return ingredients.find { it.name == name }
//    }
//
//    override suspend fun findById(id: Long): Ingredients?{
//        return ingredients.find { it.id == id }
//    }
//
//    override suspend fun findAll(): List<Ingredients> {
//        return ingredients.toList()
//    }
//
//
//}

package api.repository


import api.models.Difficulty
import api.models.IngredientUnit
import api.models.IngredientUnits
import api.models.Ingredients
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipe
import api.models.Recipes
import kotlinx.coroutines.runBlocking
import kotlin.math.E

interface IngredientUnitRepository {

}

class IngredientUnitRepositoryImpl() : IngredientUnitRepository,
    CrudImplementation<IngredientUnit, Pair<Int, String>>(
    table = IngredientUnits,
    toEntity = { row ->
        IngredientUnit(row[IngredientUnits.recipeId],
            row[IngredientUnits.ingredientName],
            row[IngredientUnits.amount],
            row[IngredientUnits.unitType]
        )},
    idColumns = listOf(IngredientUnits.recipeId, IngredientUnits.ingredientName),
    idExtractor = {listOf(IngredientUnits.recipeId, IngredientUnits.ingredientName)},
    entityMapper = { stmt, ingredientUnit ->
        stmt[IngredientUnits.recipeId] = ingredientUnit.recipeId
        stmt[IngredientUnits.ingredientName] = ingredientUnit.ingredientName
        stmt[IngredientUnits.amount] = ingredientUnit.amount
        stmt[IngredientUnits.unitType] = ingredientUnit.unitType
    }
) {}


//object FakeIngredientUnitRepository : IngredientUnitRepository {
//
//    val list = mutableListOf<IngredientUnits>()
//
//
//    init {
//        runBlocking {
//            val ingredient1 = FakeIngredientUnitRepository.create(1, 1.5, "KG")
//            val ingredient2 = FakeIngredientUnitRepository.create(2, 50.0, "GRAMS")
//            val ingredient3 = FakeIngredientUnitRepository.create(3, 10.0, "GRAMS")
//            val ingredient4 = FakeIngredientUnitRepository.create(4, 0.2, "KG")   // Lentils: 200 grams
//            val ingredient5 = FakeIngredientUnitRepository.create(5, 3.0, "GRAMS") // Tomatoes: 3 pieces
//            val ingredient6 = FakeIngredientUnitRepository.create(6, 2.0, "GRAMS")  // Spices: 2 tablespoons
//        }
//    }
//
//    suspend fun create(id: Long, amount: Double, unitType: String): IngredientUnits? {
//
//        val findIngredient = FakeIngredientsRepository.findById(id)
//        if (findIngredient == null) {
//            IllegalArgumentException()
//            return null
//        } else {
//            val newIngredientUnit = IngredientUnits(findIngredient, amount, unitType)
//            list.add(newIngredientUnit)
//            return newIngredientUnit
//        }
//    }
//}

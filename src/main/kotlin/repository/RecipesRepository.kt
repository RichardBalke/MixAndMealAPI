package api.repository

import api.models.Difficulty
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipe
import api.models.Recipes

interface RecipesRepository : CrudRepository<Recipe, Int> {
    suspend fun findByTitle(title: String): List<Recipe>
    suspend fun findByDifficulty(difficulty: String?): List<Recipe>
    suspend fun findByMealType(mealType: String?): List<Recipe>
    suspend fun findByDiets(diets: String): List<Recipe>
    suspend fun findByKitchenStyle(kitchenStyle: String): List<Recipe>

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}


class RecipesRepositoryImpl : CrudImplementation<Recipe, Int>(
    table = Recipes,
    toEntity = { row ->
        val difficultyString = row[Recipes.difficulty]
        val difficultyEnum = Difficulty.valueOf(difficultyString)
        val mealTypeString = row[Recipes.mealType]
        val mealTypeEnum = MealType.valueOf(mealTypeString)
        val kitchenStyleString = row[Recipes.kitchenStyle]
        val kitchenStyleEnum = KitchenStyle.valueOf(kitchenStyleString)
        Recipe(row[Recipes.id],
            row[Recipes.title],
            row[Recipes.description],
            row[Recipes.prepTime],
            row[Recipes.cookingTime],
            difficultyEnum,
            row[Recipes.image],
            mealTypeEnum,
            kitchenStyleEnum) },
    idColumns = listOf(Recipes.id),
    idExtractor =  { listOf(Int) },
    entityMapper = { stmt, recipe ->
        stmt[Recipes.id] = recipe.id
        stmt[Recipes.title] = recipe.title
        stmt[Recipes.description] = recipe.description
        stmt[Recipes.prepTime] = recipe.prepTime
        stmt[Recipes.cookingTime] = recipe.cookingTime
        stmt[Recipes.difficulty] = recipe.difficulty.name
        stmt[Recipes.image] = recipe.image
        stmt[Recipes.mealType] = recipe.mealType.name
        stmt[Recipes.kitchenStyle] = recipe.kitchenStyle.name
    }), RecipesRepository {

    override suspend fun findByTitle(title: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByDifficulty(difficulty: String?): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByMealType(mealType: String?): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByDiets(diets: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByKitchenStyle(kitchenStyle: String): List<Recipe> {
        TODO("Not yet implemented")
    }

}
//object FakeRecipeRepository : RecipesRepository {
//    var currentID: Long = 0
//    val recipes = mutableListOf<Recipes>()
//
//    init {
//        runBlocking {
//
//           create(
//                Recipes(
//                    title = "Creamy Mushroom Soup",
//                    description = "A rich and creamy Soup with earthy mushrooms and parmesan.",
//                    prepTime = 15,
//                    cookingTime = 300,
//                    difficulty = Difficulty.EASY,
//                    image = "https://example.com/images/Soup.jpg",
//                    mealType = MealType.DINNER,
//                    kitchenStyle = KitchenStyle.MEDITERRANEAN,
//                    diets = listOf(Diets.VEGAN),
//                    ingredients = listOf<IngredientUnits>(
//                        list[1],
//                        list[2]
//                    )
//                )
//            )
//
//            create(
//                Recipes(
//                    title = "Creamy Mushroom Risotto",
//                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
//                    prepTime = 15,
//                    cookingTime = 300,
//                    difficulty = Difficulty.MEDIUM,
//                    image = "https://example.com/images/risotto.jpg",
//                    mealType = MealType.DINNER,
//                    kitchenStyle = KitchenStyle.ITALIAN,
//                    diets = listOf(Diets.VEGAN),
//                    ingredients = listOf<IngredientUnits>(
//                        list[1],
//                        list[4]
//                    )
//                )
//            )
//
//            create(
//                Recipes(
//                    title = "Hotchpotch",
//                    description = "A rich and creamy Hotchpotch with earthy mushrooms and parmesan.",
//                    prepTime = 15,
//                    cookingTime = 300,
//                    difficulty = Difficulty.MEDIUM,
//                    image = "https://example.com/images/risotto.jpg",
//                    mealType = MealType.DINNER,
//                    kitchenStyle = KitchenStyle.ITALIAN,
//                    diets = listOf(Diets.VEGAN),
//                    ingredients = listOf<IngredientUnits>(
//                        list[5],
//                        list[3]
//                    )
//                )
//            )
//
//
//        }
//    }
//
//    override suspend fun findAll(): List<Recipes> {
//        return recipes.toList()
//    }
//
//    override suspend fun create(entity: Recipes): Recipes {
//        currentID++
//        val newRecipe = entity.copy(id = currentID)
//        recipes.add(newRecipe)
//        return newRecipe
//    }
//
//    override suspend fun findByTitle(title: String): List<Recipes> {
//        val recipeTitles = mutableListOf<Recipes>()
//        for (recipe in recipes) {
//            if (recipe.title.lowercase().contains(title.lowercase())) {
//                recipeTitles.add(recipe)
//            }
//        }
//        return recipeTitles
//    }
//
//    override suspend fun findByMealType(mealType: String?): List<Recipes> {
//        val foundRecipes = mutableListOf<Recipes>()
//        for (recipe in recipes) {
//            if (recipe.mealType?.name?.lowercase() == mealType) {
//                foundRecipes.add(recipe)
//            }
//        }
//        return foundRecipes
//    }
//
//    override suspend fun findByDifficulty(difficulty: String?): List<Recipes> {
//        val foundRecipes = mutableListOf<Recipes>()
//        for (recipe in recipes) {
//            if (recipe.difficulty.name.lowercase() == difficulty) {
//                foundRecipes.add(recipe)
//            }
//        }
//        return foundRecipes
//    }
//
//    override suspend fun findByDiets(diets: String?): List<Recipes> {
//        val foundRecipes = mutableListOf<Recipes>()
//        for (recipe in recipes) {
//            val matchedDiets = recipe.diets.any { it.displayName.lowercase() == diets?.lowercase() }
//            if (matchedDiets) {
//                foundRecipes.add(recipe)
//            }
//        }
//        return foundRecipes
//
//    }
//
//    override suspend fun findByKitchenStyle(kitchenStyle: String?): List<Recipes> {
//        val foundRecipes = mutableListOf<Recipes>()
//        for (recipe in recipes) {
//            if (recipe.kitchenStyle?.name?.lowercase() == kitchenStyle) {
//                foundRecipes.add(recipe)
//            }
//        }
//        return foundRecipes
//    }
//
//    override suspend fun findById(id: Long): Recipes? {
//        return recipes.find { it.id == id }
//    }
//
//    override suspend fun delete(id: Long): Boolean {
//        return recipes.removeIf { id == it.id}
//    }
//
//    override suspend fun update(id: Long, entity: Recipes): Recipes {
//        check(entity.id > 0) { "ID must be greater than 0." }
//        val index = recipes.indexOfFirst { it.id == entity.id }
//        require(index != -1) { "Recipe with ID ${entity.id} not found." }
//        recipes[index] = entity
//        return entity
//    }
//}
package api.repository

import api.models.Diets
import api.models.Difficulty
import api.models.IngredientUnits
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipes
import kotlinx.coroutines.runBlocking
import service.RecipeService
import api.repository.FakeIngredientUnitRepository.list

interface RecipesRepository : CrudRepository<Recipes, Long> {
    suspend fun findByTitle(title: String): List<Recipes>
    suspend fun findByDifficulty(difficulty: String?): List<Recipes>
    suspend fun findByMealType(mealType: String?): List<Recipes>
    suspend fun findByDiets(diets: String?): List<Recipes>
    suspend fun findByKitchenStyle(kitchenStyle: String?): List<Recipes>

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}

object FakeRecipeRepository : RecipesRepository {
    var currentID: Long = 0
    val recipes = mutableListOf<Recipes>()

    init {
        runBlocking {

           create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )

            create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )

            create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )


        }
    }

    override suspend fun findAll(): List<Recipes> {
        return recipes.toList()
    }

    override suspend fun create(entity: Recipes): Recipes {
        currentID++
        val newRecipe = entity.copy(id = currentID)
        recipes.add(newRecipe)
        return newRecipe
    }

    override suspend fun findByTitle(title: String): List<Recipes> {
        val recipeTitles = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.title.lowercase().contains(title.lowercase())) {
                recipeTitles.add(recipe)
            }
        }
        return recipeTitles
    }

    override suspend fun findByMealType(mealType: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.mealType?.name?.lowercase() == mealType) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByDifficulty(difficulty: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.difficulty.name.lowercase() == difficulty) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByDiets(diets: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            val matchedDiets = recipe.diets.any { it.displayName.lowercase() == diets?.lowercase() }
            if (matchedDiets) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes

    }

    override suspend fun findByKitchenStyle(kitchenStyle: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.kitchenStyle?.name?.lowercase() == kitchenStyle) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findById(id: Long): Recipes? {
        return recipes.find { it.id == id }
    }

    override suspend fun delete(id: Long): Boolean {
        return recipes.removeIf { id == it.id}
    }

    override suspend fun update(entity: Recipes) {
        check(entity.id > 0) { "ID must be greater than 0." }
        val index = recipes.indexOfFirst { it.id == entity.id }
        require(index != -1) { "Recipe with ID ${entity.id} not found." }
        recipes[index] = entity
    }
}
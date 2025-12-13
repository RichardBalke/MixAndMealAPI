package api

import api.repository.IngredientUnitRepository
import api.repository.IngredientUnitRepositoryImpl
import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.repository.UserRepository
import api.repository.UserRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import repository.AllergensRepository
import repository.AllergensRepositoryImpl
import repository.DietsRepository
import repository.DietsRepositoryImpl
import repository.RecipeAllergensRepository
import repository.RecipeAllergensRepositoryImpl
import repository.RecipeDietsRepository
import repository.RecipeDietsRepositoryImpl
import repository.RecipeImageRepository
import repository.RecipeImagesRepositoryImpl
import repository.UserAllergensRepository
import repository.UserAllergensRepositoryImpl
import repository.UserDietsRepository
import repository.UserDietsRepositoryImpl
import repository.UserFavouritesRepository
import repository.UserFavouritesRepositoryImpl
import repository.UserFridgeRepository
import repository.UserFridgeRepositoryImpl
import service.AllergenService
import service.DietsService
import service.IngredientUnitService
import service.RecipeAllergenService
import service.RecipeDietsService
import service.RecipeImagesService
import service.RecipeService
import service.UserAllergensService
import service.UserDietsService
import service.UserFavouritesService
import service.UserFridgeService
import service.UserService

fun Application.configureKoin(){
    // create all dependencies that can be injected.
    // You can use 'single' for singletons and 'factory' for multiple instances
    val appModule = module {
        single<UserRepository> { UserRepositoryImpl() }
        factory { UserService(get()) }
        single<AllergensRepository> { AllergensRepositoryImpl() }
        factory { AllergenService(get()) }
        single<DietsRepository> { DietsRepositoryImpl() }
        factory { DietsService(get()) }
        single<IngredientUnitRepository> { IngredientUnitRepositoryImpl() }
        factory { IngredientUnitService(get()) }
        single<RecipeAllergensRepository> { RecipeAllergensRepositoryImpl() }
        factory { RecipeAllergenService(get()) }
        single<RecipeDietsRepository> { RecipeDietsRepositoryImpl() }
        factory { RecipeDietsService(get()) }
        single<RecipesRepository> { RecipesRepositoryImpl() }
        factory { RecipeService(get()) }
        single<UserAllergensRepository> { UserAllergensRepositoryImpl() }
        factory { UserAllergensService(get()) }
        single<UserDietsRepository> { UserDietsRepositoryImpl() }
        factory { UserDietsService(get()) }
        single<UserFavouritesRepository> { UserFavouritesRepositoryImpl() }
        factory { UserFavouritesService(get()) }
        single<UserFridgeRepository> { UserFridgeRepositoryImpl() }
        factory { UserFridgeService(get()) }
        single<RecipeImageRepository> { RecipeImagesRepositoryImpl() }
        factory { RecipeImagesService(get()) }
    }

    install(Koin){
        slf4jLogger()
        modules(appModule)
    }
}
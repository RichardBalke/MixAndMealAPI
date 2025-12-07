package routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.RecipeImagesService
import models.dto.RecipeImageEntry
import io.ktor.http.*

fun Route.recipeImagesRoutes(recipeImagesService: RecipeImagesService) {

    route("/recipes/{recipeId}/images") {

        get {
            val recipeId = call.parameters["recipeId"]?.toIntOrNull()
                ?: return@get call.respondText("Missing or invalid recipeId", status = HttpStatusCode.BadRequest)

            val images = recipeImagesService.getImagesForRecipe(recipeId)
            call.respond(images)
        }

        post {
            val recipeId = call.parameters["recipeId"]?.toIntOrNull()
                ?: return@post call.respondText("Missing or invalid recipeId", status = HttpStatusCode.BadRequest)

            val request = call.receive<Map<String, String>>()
            val imageUrl = request["imageUrl"]
                ?: return@post call.respondText("Missing imageUrl", status = HttpStatusCode.BadRequest)

            val newImage = recipeImagesService.addImage(recipeId, imageUrl)
            call.respond(HttpStatusCode.Created, newImage)
        }

        delete("/{imageId}") {
            val imageId = call.parameters["imageId"]?.toIntOrNull()
                ?: return@delete call.respondText("Missing or invalid imageId", status = HttpStatusCode.BadRequest)

            recipeImagesService.deleteImage(imageId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
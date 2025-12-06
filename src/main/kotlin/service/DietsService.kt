package service

import models.dto.DietEntry
import repository.DietsRepository

class DietsService(private val dietsRepository: DietsRepository) {
    suspend fun getDietById(id: Int) : DietEntry? {
        return dietsRepository.findById(id)
    }
}
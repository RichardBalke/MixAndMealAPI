package service

import models.dto.AllergenEntry
import repository.AllergensRepository

class AllergenService(private val allergenRepository : AllergensRepository) {
    suspend fun getAllergenById(id: Int): AllergenEntry? {
        return allergenRepository.findById(id)
    }
}
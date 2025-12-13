package service

import api.repository.UserRepository
import models.dto.UserEntry

class UserService(private val userRepository: UserRepository) {
    suspend fun getByEmail(email: String): UserEntry? {
        return userRepository.findByEmail(email)
    }

    suspend fun getAll(): List<UserEntry> {
        return userRepository.findAll()
    }
}
package service

import api.repository.UserRepository
import models.dto.UserEntry
import org.h2.engine.User

class UserService(private val userRepository: UserRepository) {
    suspend fun getByEmail(email: String): UserEntry? {
        return userRepository.findByEmail(email)
    }
}
package api.repository

import api.models.Role
import models.dto.UserEntry
import models.tables.User
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


interface UserRepository {
    suspend fun findByUsername(username: String): UserEntry?
    suspend fun findByEmail(email: String): UserEntry?
    suspend fun getRoleById(id: String): Role
}

class UserRepositoryImpl : UserRepository, CrudImplementation<UserEntry, String>(
    table = User,
    toEntity = { row ->
        val roleString = row[User.role]
        val roleEnum = Role.valueOf(roleString)
        UserEntry(row[User.name], row[User.email], row[User.password], roleEnum) },
    idColumns = listOf(User.email),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, user ->
        stmt[User.name] = user.name
        stmt[User.email] = user.email
        stmt[User.password] = user.password
        stmt[User.role] = user.role.name
    }
) {
    override suspend fun findByUsername(username: String): UserEntry? = transaction {
        User.selectAll()
            .where { User.name eq username }
            .mapNotNull(toEntity)
            .singleOrNull()
    }
    override suspend fun findByEmail(email: String): UserEntry? = transaction {
        User.selectAll()
            .where { User.email eq email }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun getRoleById(id: String): Role {
        val user = transaction {
            User.selectAll()
                .where { User.email eq id }
                .mapNotNull(toEntity)
                .single()
        }
        return user.role
    }
}


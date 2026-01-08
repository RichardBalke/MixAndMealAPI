package api.repository

import api.models.Role
import api.responses.RecipeCardResponse
import models.dto.UserEntry
import models.tables.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


interface UserRepository : CrudRepository<UserEntry, String> {
    suspend fun findByUsername(username: String): UserEntry?
    suspend fun findByEmail(email: String): UserEntry?
    suspend fun getRoleById(id: String): Role
}

class UserRepositoryImpl : UserRepository, CrudImplementation<UserEntry, String>(
    table = Users,
    toEntity = { row ->
        val roleString = row[Users.role]
        val roleEnum = Role.valueOf(roleString)
        UserEntry(row[Users.name], row[Users.email], row[Users.password], roleEnum) },
    idColumns = listOf(Users.email),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, user ->
        stmt[Users.name] = user.name
        stmt[Users.email] = user.email
        stmt[Users.password] = user.password
        stmt[Users.role] = user.role.name
    }
) {
    override suspend fun findByUsername(username: String): UserEntry? = transaction {
        Users.selectAll()
            .where { Users.name eq username }
            .mapNotNull(toEntity)
            .singleOrNull()
    }
    override suspend fun findByEmail(email: String): UserEntry? = transaction {
        Users.selectAll()
            .where { Users.email eq email }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun getRoleById(id: String): Role {
        val user = transaction {
            Users.selectAll()
                .where { Users.email eq id }
                .mapNotNull(toEntity)
                .single()
        }
        return user.role
    }


}


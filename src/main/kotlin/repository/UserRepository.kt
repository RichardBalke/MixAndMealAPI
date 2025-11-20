package api.repository

import api.models.Role
import api.models.User
import api.models.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


interface UserRepository : CrudRepository<User, Int> {
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun getRoleById(id: Int): Role
}

class UserRepositoryImpl : CrudImplementation<User, Int>(
    table = Users,
    toEntity = { row ->
        val roleString = row[Users.role]
        val roleEnum = Role.valueOf(roleString)
        User(row[Users.user_id], row[Users.name], row[Users.email], row[Users.password], roleEnum) },
    idColumn = Users.user_id,
    entityMapper = { stmt, user ->
        stmt[Users.user_id] = user.id
        stmt[Users.name] = user.name
        stmt[Users.email] = user.email
        stmt[Users.password] = user.password
        stmt[Users.role] = user.role.name
    }
), UserRepository {
    override suspend fun findByUsername(username: String): User? = transaction {
        Users.selectAll()
            .where { Users.name eq username }
            .mapNotNull(toEntity)
            .singleOrNull()
    }
    override suspend fun findByEmail(email: String): User? = transaction {
        Users.selectAll()
            .where { Users.email eq email }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun getRoleById(id: Int): Role {
        val user = transaction {
            Users.selectAll()
                .where { Users.user_id eq id }
                .mapNotNull(toEntity)
                .single()
        }
        return user.role
    }
}

//object FakeUserRepository : UserRepository {
//    public var currentID: Long = 0L
//    public val users = mutableListOf<User>()
//
//    init {
//        runBlocking {
//            create(User("Bart", "Test1", "test1@test.nl", Role.ADMIN))
//            create(User("Fauve", "Test2", "test2@test.nl", Role.USER))
//            create(User("Richard", "Test3", "test3@test.nl", Role.USER))
//            create(User("Yoran", "Test4", "test4@test.nl", Role.USER))
//        }
//    }
//
//    override suspend fun create(entity: User): User {
//        currentID++
//        val newUser = entity.copy(id = currentID)
//        users.add(newUser)
//        return newUser
//    }
//
//
//    //Get functies
//    override suspend fun findById(id: Long): User? {
//        return users.find { it.id == id }
//    }
//
//    override suspend fun findByUsername(username: String): User? {
//        return users.find { it.name == username }
//    }
//
//    override suspend fun findByEmail(email: String): User? {
//        return users.find { it.email == email }
//    }
//
//    override suspend fun findAll(): List<User> {
//        return users.toList()
//    }
//
//    override suspend fun delete(id: Long): Boolean {
//        return users.removeIf { id == it.id}
//    }
//
//    override suspend fun update(entity: User) {
//        val index = users.indexOfFirst { it.id == entity.id }
//        if (index != -1) {
//            users[index] = entity
//        }
//    }
//
//    override suspend fun getRoleById(id: Long): Role {
//        return findById(id)?.role ?: Role.USER
//    }
//}


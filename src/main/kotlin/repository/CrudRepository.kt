package api.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

interface CrudRepository <T,ID> {
    suspend fun findById(id: ID): T?
    suspend fun create(entity: T): T
    suspend fun findAll(): List<T>
    suspend fun update(id: ID, entity: T): T
    suspend fun delete(id: ID): Boolean
}

abstract class CrudImplementation<T : Any,ID : Any>(
    private val table : Table,
    protected val toEntity: (ResultRow) -> T,
    private val idColumn: Column<ID>,
    private val entityMapper: (UpdateBuilder<*>, T) -> Unit
) : CrudRepository<T, ID> {

    override suspend fun findById(id: ID): T? = transaction {
        table.selectAll()
            .where { idColumn eq id }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun findAll(): List<T> = transaction {
        table.selectAll().map(toEntity)
    }

    override suspend fun update(id: ID, entity: T): T = transaction {
        // Perform update with where clause targeting entity id
        val updatedRows = table.update({ idColumn eq id }) {
            entityMapper(it, entity)
        }
        // Optionally, check if update happened
        require(updatedRows > 0) { "No rows updated" }

        // Fetch updated entity from DB (could be a select by id)
        val updatedEntityRow = table.selectAll()
            .where { idColumn eq id }.single()
        toEntity(updatedEntityRow)
    }

    override suspend fun create(entity: T): T = transaction {
        val inserted = table.insert {
            entityMapper(it, entity)
        }
        toEntity(inserted.resultedValues!!.first())
    }

    override suspend fun delete(id: ID): Boolean = transaction {
        table.deleteWhere { idColumn eq id } > 0
    }
}



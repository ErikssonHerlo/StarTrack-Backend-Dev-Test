package stsa.kotlin_htmx.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Key
import stsa.kotlin_htmx.persistence.KeyEntity

class KeyRepository {

    fun findAll(): List<Key> = transaction {
        KeyEntity.selectAll().map { toKey(it) }
    }

    fun findById(id: String): Key? = transaction {
        KeyEntity.select { KeyEntity.id eq id }
            .map { toKey(it) }
            .singleOrNull()
    }

    fun findByName(name: String): List<Key> = transaction {
        KeyEntity.select { KeyEntity.name like "%$name%" }
            .map { toKey(it) }
    }

    fun save(key: Key): Key = transaction {
        KeyEntity.insert {
            it[id] = key.id
            it[name] = key.name
            it[description] = key.description
            it[image] = key.image
            it[crates] = key.crates
        }
        key
    }

    fun count(): Long = transaction {
        KeyEntity.selectAll().count()
    }

    private fun toKey(row: ResultRow): Key = Key(
        id = row[KeyEntity.id],
        name = row[KeyEntity.name],
        description = row[KeyEntity.description],
        image = row[KeyEntity.image],
        crates = row[KeyEntity.crates]
    )
}

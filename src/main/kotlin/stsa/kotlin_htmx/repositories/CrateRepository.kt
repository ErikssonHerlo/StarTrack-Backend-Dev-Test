package stsa.kotlin_htmx.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.persistence.CrateEntity

class CrateRepository {

    fun findAll(): List<Crate> = transaction {
        CrateEntity.selectAll().map { toCrate(it) }
    }

    fun findById(id: String): Crate? = transaction {
        CrateEntity.select { CrateEntity.id eq id }
            .map { toCrate(it) }
            .singleOrNull()
    }

    fun findByName(name: String): List<Crate> = transaction {
        CrateEntity.select { CrateEntity.name like "%$name%" }
            .map { toCrate(it) }
    }

    fun save(crate: Crate): Crate = transaction {
        CrateEntity.insert {
            it[id] = crate.id
            it[name] = crate.name
            it[description] = crate.description
            it[image] = crate.image
        }
        crate
    }

    fun count(): Long = transaction {
        CrateEntity.selectAll().count()
    }

    private fun toCrate(row: ResultRow): Crate = Crate(
        id = row[CrateEntity.id],
        name = row[CrateEntity.name],
        description = row[CrateEntity.description],
        image = row[CrateEntity.image]
    )
}

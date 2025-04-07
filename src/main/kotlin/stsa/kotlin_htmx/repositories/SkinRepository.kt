package stsa.kotlin_htmx.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.persistence.SkinEntity
import stsa.kotlin_htmx.domain.models.Skin

class SkinRepository {

    fun findAll(): List<Skin> = transaction {
        SkinEntity.selectAll().map { toSkin(it) }
    }

    fun findById(id: String): Skin? = transaction {
        SkinEntity.select { SkinEntity.id eq id }
            .map { toSkin(it) }
            .singleOrNull()
    }

    fun save(skin: Skin): Skin = transaction {
        SkinEntity.insert {
            it[SkinEntity.id] = skin.id
            it[SkinEntity.name] = skin.name
            it[SkinEntity.description] = skin.description
            it[SkinEntity.team] = skin.team
            it[SkinEntity.image] = skin.image
            it[SkinEntity.crates] = skin.crates
        }
        skin
    }

    fun update(skin: Skin): Boolean = transaction {
        SkinEntity.update({ SkinEntity.id eq skin.id }) {
            it[name] = skin.name
            it[description] = skin.description
            it[team] = skin.team
            it[image] = skin.image
            it[crates] = skin.crates
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        SkinEntity.deleteWhere { SkinEntity.id eq id } > 0
    }

    fun count(): Long = transaction {
        SkinEntity.selectAll().count()
    }

    private fun toSkin(row: ResultRow): Skin {
        return Skin(
            id = row[SkinEntity.id],
            name = row[SkinEntity.name],
            description = row[SkinEntity.description],
            team = row[SkinEntity.team],
            image = row[SkinEntity.image],
            crates = row[SkinEntity.crates]
        )
    }
}

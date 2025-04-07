package stsa.kotlin_htmx.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.persistence.SkinsEntity
import stsa.kotlin_htmx.domain.models.Skin

class SkinRepository {

    fun findAll(): List<Skin> = transaction {
        SkinsEntity.selectAll().map { toSkin(it) }
    }

    fun findById(id: String): Skin? = transaction {
        SkinsEntity.select { SkinsEntity.id eq id }
            .map { toSkin(it) }
            .singleOrNull()
    }

    fun save(skin: Skin): Skin = transaction {
        SkinsEntity.insert {
            it[SkinsEntity.id] = skin.id
            it[SkinsEntity.name] = skin.name
            it[SkinsEntity.description] = skin.description
            it[SkinsEntity.team] = skin.team
            it[SkinsEntity.image] = skin.image
            it[SkinsEntity.crates] = skin.crates
        }
        skin
    }

    fun update(skin: Skin): Boolean = transaction {
        SkinsEntity.update({ SkinsEntity.id eq skin.id }) {
            it[name] = skin.name
            it[description] = skin.description
            it[team] = skin.team
            it[image] = skin.image
            it[crates] = skin.crates
        } > 0
    }

    fun delete(id: String): Boolean = transaction {
        SkinsEntity.deleteWhere { SkinsEntity.id eq id } > 0
    }

    fun count(): Long = transaction {
        SkinsEntity.selectAll().count()
    }

    private fun toSkin(row: ResultRow): Skin {
        return Skin(
            id = row[SkinsEntity.id],
            name = row[SkinsEntity.name],
            description = row[SkinsEntity.description],
            team = row[SkinsEntity.team],
            image = row[SkinsEntity.image],
            crates = row[SkinsEntity.crates]
        )
    }
}

package stsa.kotlin_htmx.persistence

import org.jetbrains.exposed.sql.Table
import stsa.kotlin_htmx.persistence.SkinEntity.nullable

object AgentEntity : Table("agent") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val team = text("team").nullable()
    val image = text("image").nullable()

    override val primaryKey = PrimaryKey(id)
}

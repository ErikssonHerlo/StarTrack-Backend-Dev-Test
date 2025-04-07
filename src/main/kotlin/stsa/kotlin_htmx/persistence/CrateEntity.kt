package stsa.kotlin_htmx.persistence

import org.jetbrains.exposed.sql.Table

object CrateEntity : Table("crate") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val image = text("image").nullable()

    override val primaryKey = PrimaryKey(id)
}
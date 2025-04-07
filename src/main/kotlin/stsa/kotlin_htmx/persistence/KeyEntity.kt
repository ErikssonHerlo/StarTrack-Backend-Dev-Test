package stsa.kotlin_htmx.persistence

import org.jetbrains.exposed.sql.Table

object KeyEntity : Table("key") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val image = text("image").nullable()
    val crates = text("crates").nullable()  // Stored as TEXT

    override val primaryKey = PrimaryKey(id)
}
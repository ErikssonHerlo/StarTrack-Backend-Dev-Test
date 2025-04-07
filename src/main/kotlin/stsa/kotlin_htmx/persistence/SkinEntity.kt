package stsa.kotlin_htmx.persistence

import org.jetbrains.exposed.sql.Table

object SkinEntity : Table("skin") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val team = varchar("team", 255).nullable()
    val image = text("image").nullable()
    val crates = text("crates").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Skin_ID")
}

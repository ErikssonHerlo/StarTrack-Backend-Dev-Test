package stsa.kotlin_htmx.domain.models

data class Skin(
    val id: String,
    val name: String,
    val description: String?,
    val team: String?,
    val image: String?,
    val crates: String?
)
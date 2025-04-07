package stsa.kotlin_htmx.domain.models

data class Key(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val crates: String? // JSON stored as a String
)
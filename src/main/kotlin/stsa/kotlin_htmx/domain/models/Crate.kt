package stsa.kotlin_htmx.domain.models

data class Crate(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?
)
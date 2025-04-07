package stsa.kotlin_htmx.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Crate(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?
)
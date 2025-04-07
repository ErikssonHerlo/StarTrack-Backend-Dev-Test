package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class SecondaryCrateDto(
    val id: String,
    val name: String,
    val image: String? = null
)
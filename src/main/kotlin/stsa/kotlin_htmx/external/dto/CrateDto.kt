package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class CrateDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val image: String? = null
)
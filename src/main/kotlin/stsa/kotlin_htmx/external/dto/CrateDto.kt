package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class CrateDto(
    val id: String,
    val name: String,
    val image: String? = null
)
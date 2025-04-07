package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val id: String,
    val name: String
)

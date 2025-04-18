package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class AgentDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val team: TeamDto? = null,
    val image: String? = null
)

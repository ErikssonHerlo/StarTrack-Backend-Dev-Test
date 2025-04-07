package stsa.kotlin_htmx.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Agent(
    val id: String,
    val name: String,
    val description: String?,
    val team: String?,
    val image: String?
)
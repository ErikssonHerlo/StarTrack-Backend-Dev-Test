package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class SkinDto(
    val id: String,
    val name: String,
    val description: String? = null,
    // Only requested fields are included in the DTO to reduce payload size
    val crates: List<CrateDto> = emptyList(),
    val team: TeamDto? = null,
    val image: String? = null
)

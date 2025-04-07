package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable
import stsa.kotlin_htmx.external.dto.AgentDto

@Serializable
data class AgentExportRequestDto(
    val data: List<AgentDto>
)

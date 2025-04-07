package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable
import stsa.kotlin_htmx.external.dto.SkinDto

@Serializable
data class SkinExportRequestDto(
    val data: List<SkinDto>
)

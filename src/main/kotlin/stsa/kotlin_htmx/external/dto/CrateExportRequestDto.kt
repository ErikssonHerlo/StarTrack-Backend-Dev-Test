package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class CrateExportRequestDto (
    val data: List<CrateDto>
)

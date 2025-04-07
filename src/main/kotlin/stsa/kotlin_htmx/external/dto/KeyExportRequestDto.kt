package stsa.kotlin_htmx.external.dto

import kotlinx.serialization.Serializable

@Serializable
data class KeyExportRequestDto (
    val data: List<KeyDto>
)
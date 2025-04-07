package stsa.kotlin_htmx.external.dto


import kotlinx.serialization.Serializable

@Serializable
data class KeyDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val crates: List<SecondaryCrateDto> = emptyList(),
    val image: String? = null
)
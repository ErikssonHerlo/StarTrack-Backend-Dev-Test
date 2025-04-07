package stsa.kotlin_htmx.domain.models
import kotlinx.serialization.Serializable
import stsa.kotlin_htmx.external.dto.SkinDto

@Serializable
data class Skin(
    val id: String,
    val name: String,
    val description: String?,
    val team: String?,
    val image: String?,
    val crates: String?
)
package stsa.kotlin_htmx.utils

import kotlinx.html.S
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import stsa.kotlin_htmx.domain.models.Agent
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.domain.models.Key
import stsa.kotlin_htmx.domain.models.Skin
import stsa.kotlin_htmx.external.dto.*

/**
 * Converts a domain model Skin to a SkinDto.
 */
fun Skin.convertToDto(): SkinDto {
    // Parse the 'crates' JSON string into a list of CrateDto objects
    val cratesList: List<SecondaryCrateDto> = this.crates?.let { jsonString ->
        try {
            Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    } ?: emptyList()

    // Wrap the team string into a TeamDto if available
    val teamDto: TeamDto? = this.team?.let { teamName ->
        TeamDto(id = "", name = teamName)
    }

    return SkinDto(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image,
        crates = cratesList,
        team = teamDto
    )
}

fun Agent.convertToDto(): AgentDto {
    // Wrap the team name into a TeamDto if present; otherwise, null.
    val teamDto = this.team?.let { TeamDto(id = "", name = it) }
    return AgentDto(
        id = this.id,
        name = this.name,
        description = this.description,
        team = teamDto,
        image = this.image
    )
}

/**
 * Converts a domain model Crate to a CrateDto.
 */
fun Crate.convertToDto(): CrateDto = CrateDto(
    id = this.id,
    name = this.name,
    description = this.description,
    image = this.image
)

/**
 * Converts a domain model Key to a KeyDto.
 */
fun Key.convertToDto(): KeyDto {
    // Parse the 'crates' field (stored as JSON string) into a list of CrateDto.
    val cratesList: List<SecondaryCrateDto> = this.crates?.let { jsonString ->
        try {
            Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    } ?: emptyList()

    return KeyDto(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image,
        crates = cratesList
    )
}

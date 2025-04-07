package stsa.kotlin_htmx.external
import stsa.kotlin_htmx.external.dto.AgentDto
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.external.dto.KeyDto
import stsa.kotlin_htmx.external.dto.SkinDto

/**
 * Interface for the CSGO API client.
 * This interface defines the methods to interact with the CSGO API.
 */
interface CSGOApiClientInterface {
    suspend fun getSkins(): List<SkinDto>
    suspend fun getAgents(): List<AgentDto>
    suspend fun getCrates(): List<CrateDto>
    suspend fun getKeys(): List<KeyDto>
}
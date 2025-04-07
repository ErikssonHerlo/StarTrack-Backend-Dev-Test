package stsa.kotlin_htmx.external
import stsa.kotlin_htmx.external.dto.*

/**
 * Interface for the CSGO API client.
 * This interface defines the methods to interact with the CSGO API.
 */
interface CSGOApiClientInterface {
    suspend fun getSkins(): List<SkinDto>
    suspend fun getAgents(): List<AgentDto>
    suspend fun getCrates(): List<PrincipalCrateDto>
    suspend fun getKeys(): List<KeyDto>
}
package stsa.kotlin_htmx.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Agent
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.AgentRepository

class AgentService(
    private val apiClient: CSGOApiClientInterface,
    private val repository: AgentRepository
) {
    private val logger = LoggerFactory.getLogger(AgentService::class.java)

    /**
     * Loads agents data from the API and saves it to the database.
     */
    suspend fun loadAgentsData() {
        try {
            val count = repository.count()
            if (count > 0) {
                logger.info("The agents table already contains data (count = $count). Skipping data load.")
                return
            }
            logger.info("The agents table is empty. Starting data load...")

            val agentsDto = apiClient.getAgents()
            val agents: List<Agent> = agentsDto.map { dto ->
                Agent(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    team = Json.encodeToString(dto.team),
                    image = dto.image
                )
            }

            transaction {
                agents.forEach { agent ->
                    repository.save(agent)
                    logger.info("Agent inserted with id: ${agent.id}")
                }
            }

            logger.info("Data load completed: ${agents.size} agents inserted into the database.")
        } catch (e: Exception) {
            logger.error("Error on loading agents data: ${e.message}", e)
        }
    }
}

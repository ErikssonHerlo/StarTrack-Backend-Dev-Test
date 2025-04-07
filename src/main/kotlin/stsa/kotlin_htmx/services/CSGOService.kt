package stsa.kotlin_htmx.services

import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Skin
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.SkinRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Agent
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.domain.models.Key
import stsa.kotlin_htmx.repositories.AgentRepository
import stsa.kotlin_htmx.repositories.CrateRepository
import stsa.kotlin_htmx.repositories.KeyRepository
import stsa.kotlin_htmx.utils.convertToDto
import kotlin.math.log

class CSGOService(
    private val apiClient: CSGOApiClientInterface,
    private val skinRepository: SkinRepository,
    private val crateRepository: CrateRepository,
    private val agentRepository: AgentRepository,
    private val keyRepository: KeyRepository
) {
    private val logger = LoggerFactory.getLogger(SkinService::class.java)
    /**
     * Loads skins data from the API and saves it to the database.
     * This function fetches the skins data from the API, transforms it into
     * domain models, and then persists it in the database.
     */
    suspend fun loadSkinsData() {

        try {
            val count = skinRepository.count()
            if (count > 0) {
                logger.info("The skin table already contains data (count = $count). Skipping data load.")
                return
            }
            logger.info("The skin table is empty. Starting data load...")

            // Get Skins list from API as DTOs
            val skinsDto = apiClient.getSkins()

            // Transform each DTO into a domain model
            val skins: List<Skin> = skinsDto.map { dto ->
                Skin(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    team = Json.encodeToString(dto.team),
                    image = dto.image,
                    // Convert the list of crates to a comma-separated string
                    crates = Json.encodeToString(dto.crates)
                )
            }

            // Insert each skin into the database using the repository & transaction block
            transaction {
                skins.forEach { skin ->
                    skinRepository.save(skin)
                    logger.info("Skin Inserted with id: ${skin.id}")
                }
            }
        } catch (e: Exception) {
            logger.error("Error loading skins data: ${e.message}", e)
        }
    }

    /**
     * Loads agents data from the API and saves it to the database.
     */
    suspend fun loadAgentsData() {
        try {
            val count = agentRepository.count()
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
                    agentRepository.save(agent)
                    logger.info("Agent inserted with id: ${agent.id}")
                }
            }

            logger.info("Data load completed: ${agents.size} agents inserted into the database.")
        } catch (e: Exception) {
            logger.error("Error on loading agents data: ${e.message}", e)
        }
    }

    /**
     * Loads crate data from the API if the crate table is empty.
     */
    suspend fun loadCratesData() {
        try {
            val count = crateRepository.count()
            if (count > 0) {
                logger.info("The crate table already contains data (count = $count). Skipping data load.")
                return
            }
            logger.info("The crate table is empty. Starting data load...")

            val cratesDto = apiClient.getCrates()
            val crates: List<Crate> = cratesDto.map { dto ->
                Crate(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    image = dto.image
                )
            }

            transaction {
                crates.forEach { crate ->
                    crateRepository.save(crate)
                    logger.info("Inserted crate with id: ${crate.id}")
                }
            }
            logger.info("Data load complete: Inserted ${crates.size} crates.")
        } catch (e: Exception) {
            logger.error("Error loading crate data: ${e.message}", e)
        }
    }
    /**
     * Loads key data from the API if the key table is empty.
     */
    suspend fun loadKeysData() {
        try {
            val count = keyRepository.count()
            if (count > 0) {
                logger.info("The key table already contains data (count = $count). Skipping data load.")
                return
            }
            logger.info("The key table is empty. Starting key data load...")

            val keysDto = apiClient.getKeys()
            val keys: List<Key> = keysDto.map { dto ->
                Key(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    image = dto.image,
                    // Convert the list of crates to a JSON string
                    crates = Json.encodeToString(dto.crates)
                )
            }

            transaction {
                keys.forEach { key ->
                    keyRepository.save(key)
                    logger.info("Inserted key with id: ${key.id}")
                }
            }
            logger.info("Key data load complete: Inserted ${keys.size} keys.")
        } catch (e: Exception) {
            logger.error("Error loading key data: ${e.message}", e)
        }
    }
}

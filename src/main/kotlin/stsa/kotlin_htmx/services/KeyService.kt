package stsa.kotlin_htmx.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Key
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.KeyRepository

class KeyService(
    private val apiClient: CSGOApiClientInterface,
    private val repository: KeyRepository
) {
    private val logger = LoggerFactory.getLogger(KeyService::class.java)

    /**
     * Loads key data from the API if the key table is empty.
     */
    suspend fun loadKeyData() {
        try {
            val count = repository.count()
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
                    repository.save(key)
                    logger.info("Inserted key with id: ${key.id}")
                }
            }
            logger.info("Key data load complete: Inserted ${keys.size} keys.")
        } catch (e: Exception) {
            logger.error("Error loading key data: ${e.message}", e)
        }
    }
}

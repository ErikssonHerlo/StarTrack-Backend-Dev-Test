package stsa.kotlin_htmx.services

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.CrateRepository

class CrateService(
    private val apiClient: CSGOApiClientInterface,
    private val repository: CrateRepository
) {
    private val logger = LoggerFactory.getLogger(CrateService::class.java)

    /**
     * Loads crate data from the API if the crate table is empty.
     */
    suspend fun loadCrateData() {
        try {
            val count = repository.count()
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
                    repository.save(crate)
                    logger.info("Inserted crate with id: ${crate.id}")
                }
            }
            logger.info("Data load complete: Inserted ${crates.size} crates.")
        } catch (e: Exception) {
            logger.error("Error loading crate data: ${e.message}", e)
        }
    }
}

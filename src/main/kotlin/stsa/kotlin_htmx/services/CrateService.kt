package stsa.kotlin_htmx.services

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.external.dto.AgentDto
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.repositories.CrateRepository
import stsa.kotlin_htmx.utils.convertToDto
import java.util.concurrent.TimeUnit

class CrateService(
    private val repository: CrateRepository
) {
    private val logger = LoggerFactory.getLogger(CrateService::class.java)

    /**
     * Retrieves all crates from the database and converts them to CrateDto.
     */
    fun getAllCrates(): List<stsa.kotlin_htmx.external.dto.CrateDto> = transaction {
        CrateRepository().findAll().map { it.convertToDto() }
    }

    /**
     * Converts a list of CrateDto to an XML string.
     */
    fun cratesExportToXmlFromRequest(crates: List<CrateDto>): String {
        val builder = StringBuilder()
        builder.append("<crates>")
        for (crate in crates) {
            builder.append("<crate>")
            builder.append("<id>${crate.id}</id>")
            builder.append("<name>${crate.name}</name>")
            builder.append("<description>${crate.description ?: ""}</description>")
            builder.append("<image>${crate.image ?: ""}</image>")
            builder.append("</crate>")
        }
        builder.append("</crates>")
        logger.info("Exported ${crates.size} crates to XML.")
        return builder.toString()
    }

    /**
     * Caches the crate data for 10 minutes to improve performance.
     */
    private val crateCache = Caffeine.newBuilder()
        .maximumSize(50) // Maximum size of the cache
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<String, List<CrateDto>>()


    /**
     * Retrieves crate by name from the cache or database.
     */
    fun getCratesByName(name: String): List<CrateDto> = crateCache.get(name) {
        val normalized = name.trim().lowercase()

        crateCache.get(normalized) {
            transaction {
                repository.findByName(normalized).map { it.convertToDto() }
            }
        }
    }
}

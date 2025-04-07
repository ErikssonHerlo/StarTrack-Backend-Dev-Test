package stsa.kotlin_htmx.services

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Key
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.external.dto.KeyDto
import stsa.kotlin_htmx.repositories.KeyRepository
import stsa.kotlin_htmx.utils.convertToDto
import java.util.concurrent.TimeUnit

class KeyService(
    private val repository: KeyRepository
) {
    private val logger = LoggerFactory.getLogger(KeyService::class.java)

    /**
     * Retrieves all keys from the database and converts them to KeyDto.
     */
    fun getAllKeys(): List<stsa.kotlin_htmx.external.dto.KeyDto> = transaction {
        KeyRepository().findAll().map { it.convertToDto() }
    }

    /**
     * Converts a list of KeyDto to an XML string.
     */
    fun keysExportToXmlFromRequest(keys: List<KeyDto>): String {
        val builder = StringBuilder()
        builder.append("<keys>")
        for (key in keys) {
            builder.append("<key>")
            builder.append("<id>${key.id}</id>")
            builder.append("<name>${key.name}</name>")
            builder.append("<description>${key.description ?: ""}</description>")
            builder.append("<image>${key.image ?: ""}</image>")
            builder.append("<crates>")
            key.crates.forEach { crate ->
                builder.append("<crate>")
                builder.append("<id>${crate.id}</id>")
                builder.append("<name>${crate.name}</name>")
                builder.append("<image>${crate.image ?: ""}</image>")
                builder.append("</crate>")
            }
            builder.append("</crates>")
            builder.append("</key>")
        }
        builder.append("</keys>")
        logger.info("Exported ${keys.size} keys to XML.")
        return builder.toString()
    }

    /**
     * Caches the Keys data for 10 minutes to improve performance.
     */
    private val keyCache = Caffeine.newBuilder()
        .maximumSize(50) // Maximum size of the cache
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<String, List<KeyDto>>()


    /**
     * Retrieves Keys by name from the cache or database.
     */
    fun getKeysByName(name: String): List<KeyDto> = keyCache.get(name) {
        val normalized = name.trim().lowercase()

        keyCache.get(normalized) {
            transaction {
                repository.findByName(normalized).map { it.convertToDto() }
            }
        }
    }
}

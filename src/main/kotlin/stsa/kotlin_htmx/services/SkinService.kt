package stsa.kotlin_htmx.services

import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Skin
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.SkinRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.external.dto.SkinDto
import stsa.kotlin_htmx.utils.convertToDto
import java.util.concurrent.TimeUnit
import com.github.benmanes.caffeine.cache.Caffeine

class SkinService(
    private val repository: SkinRepository
) {
    private val logger = LoggerFactory.getLogger(SkinService::class.java)

    /**
     * Retrieves all skins from the database and converts them to SkinDto.
     */
    fun getAllSkins(): List<stsa.kotlin_htmx.external.dto.SkinDto> = transaction {
        repository.findAll().map { it.convertToDto() }
    }

    /**
     * Maps the skin data from the database to an XML structure.
     * This method uses the SkinDto (including its nested TeamDto and CrateDto) to build the XML.
     */
    fun skinsExportToXmlFromRequest(skins: List<SkinDto>): String {
        val builder = StringBuilder()
        builder.append("<skins>")
        for (skin in skins) {
            builder.append("<skin>")
            builder.append("<id>${skin.id}</id>")
            builder.append("<name>${skin.name}</name>")
            builder.append("<description>${skin.description ?: ""}</description>")
            builder.append("<image>${skin.image ?: ""}</image>")
            // Map team info
            builder.append("<team>")
            if (skin.team != null) {
                builder.append("<id>${skin.team.id}</id>")
                builder.append("<name>${skin.team.name}</name>")
            }
            builder.append("</team>")
            // Map crates list
            builder.append("<crates>")
            skin.crates.forEach { crate ->
                builder.append("<crate>")
                builder.append("<id>${crate.id}</id>")
                builder.append("<name>${crate.name}</name>")
                builder.append("<image>${crate.image ?: ""}</image>")
                builder.append("</crate>")
            }
            builder.append("</crates>")
            builder.append("</skin>")
        }
        builder.append("</skins>")
        return builder.toString()
    }

    private val skinCache = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<String, List<SkinDto>>()


    fun getSkinsByName(name: String): List<SkinDto> = skinCache.get(name) {
        transaction {
            repository.findByName(name).map { it.convertToDto() }
        }
    }
}


package stsa.kotlin_htmx.services

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Crate
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.repositories.CrateRepository
import stsa.kotlin_htmx.utils.convertToDto

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

}

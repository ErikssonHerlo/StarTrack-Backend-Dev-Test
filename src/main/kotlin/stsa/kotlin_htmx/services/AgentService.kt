package stsa.kotlin_htmx.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import stsa.kotlin_htmx.domain.models.Agent
import stsa.kotlin_htmx.external.dto.AgentDto
import stsa.kotlin_htmx.repositories.AgentRepository
import stsa.kotlin_htmx.utils.convertToDto

class AgentService(
    private val repository: AgentRepository
) {
    private val logger = LoggerFactory.getLogger(AgentService::class.java)

    /**
     * Retrieves all agents from the database and converts them to AgentDto.
     */
    fun getAllAgents(): List<stsa.kotlin_htmx.external.dto.AgentDto> = transaction {
        AgentRepository().findAll().map { it.convertToDto() }
    }

    /**
     * Maps a list of AgentDto to an XML string.
     *
     * Expected XML structure:
     * <data>
     *   <agent>
     *     <id>agent-id</id>
     *     <name>agent name</name>
     *     <description>agent description</description>
     *     <image>agent image</image>
     *     <team>
     *       <id>team id</id>
     *       <name>team name</name>
     *     </team>
     *   </agent>
     *   ...
     * </data>
     */
    fun agentsExportToXmlFromRequest(agents: List<AgentDto>): String {
        val builder = StringBuilder()
        builder.append("<data>")
        for (agent in agents) {
            builder.append("<agent>")
            builder.append("<id>${agent.id}</id>")
            builder.append("<name>${agent.name}</name>")
            builder.append("<description>${agent.description ?: ""}</description>")
            builder.append("<image>${agent.image ?: ""}</image>")
            builder.append("<team>")
            if (agent.team != null) {
                builder.append("<id>${agent.team.id}</id>")
                builder.append("<name>${agent.team.name}</name>")
            }
            builder.append("</team>")
            builder.append("</agent>")
        }
        builder.append("</data>")
        logger.info("Exported ${agents.size} agents to XML.")
        return builder.toString()
    }

}

package stsa.kotlin_htmx.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Agent
import stsa.kotlin_htmx.persistence.AgentEntity

class AgentRepository {
    fun findAll(): List<Agent> = transaction {
        AgentEntity.selectAll().map { toAgent(it) }
    }

    fun findById(id: String): Agent? = transaction {
        AgentEntity.select { AgentEntity.id eq id }
            .map { toAgent(it) }
            .singleOrNull()
    }

    fun save(agent: Agent): Agent = transaction {
        AgentEntity.insert {
            it[id] = agent.id
            it[name] = agent.name
            it[description] = agent.description
            it[team] = agent.team
            it[image] = agent.image
        }
        agent
    }

    fun count(): Long = transaction {
        AgentEntity.selectAll().count()
    }

    fun findByName(name: String): List<Agent> = transaction {
        AgentEntity.select { AgentEntity.name like "%$name%" }
            .map { toAgent(it) }
    }

    private fun toAgent(row: ResultRow): Agent = Agent(
        id = row[AgentEntity.id],
        name = row[AgentEntity.name],
        description = row[AgentEntity.description],
        team = row[AgentEntity.team],
        image = row[AgentEntity.image]
    )
}

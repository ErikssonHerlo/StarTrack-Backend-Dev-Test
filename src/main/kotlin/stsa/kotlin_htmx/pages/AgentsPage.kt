package stsa.kotlin_htmx.pages

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.json.*
import stsa.kotlin_htmx.external.dto.AgentDto
import stsa.kotlin_htmx.api.ApiResponse

class AgentsPage {
    private val client = HttpClient()
    private val baseUrl = System.getenv("BASE_URL") ?: "http://localhost:8080"

    suspend fun render(ctx: RoutingContext) {
        val response: HttpResponse = client.get("$baseUrl/api/v1/agents")
        val json = Json.decodeFromString<ApiResponse<List<AgentDto>>>(response.bodyAsText())

        ctx.call.respondHtmlTemplate(MainTemplate(template = AgentsTemplate(json.data ?: emptyList()), "Agents List")) {
            mainSectionTemplate {
                agentsListContent {}
            }
        }
    }
}

class AgentsTemplate(private val agents: List<AgentDto>) : Template<FlowContent> {
    val agentsListContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 { +"List of Agents" }
        div {
            agents.forEach { agent ->
                div("box") {
                    h3 { +agent.name }
                    agent.image?.let {
                        img {
                            src = it
                            alt = agent.name ?: "Agent Image"
                            width = "200"
                        }
                    }
                    p { +agent.description.orEmpty() }
                    agent.team?.let {
                        h4 { +"Team: "}
                        p { + it.name }
                    }
                }
            }
        }
        insert(agentsListContent)
    }
}

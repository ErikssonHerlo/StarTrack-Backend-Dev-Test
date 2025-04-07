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
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.api.ApiResponse

class CratesPage {
    private val client = HttpClient()
    private val baseUrl = System.getenv("BASE_URL") ?: "http://localhost:8080"

    suspend fun render(ctx: RoutingContext) {
        val response: HttpResponse = client.get("$baseUrl/api/v1/crates")
        val json = Json.decodeFromString<ApiResponse<List<CrateDto>>>(response.bodyAsText())

        ctx.call.respondHtmlTemplate(MainTemplate(template = CratesTemplate(json.data ?: emptyList()), "Crates List")) {
            mainSectionTemplate {
                cratesListContent {}
            }
        }
    }
}

class CratesTemplate(private val crates: List<CrateDto>) : Template<FlowContent> {
    val cratesListContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 { +"Lista de Crates" }
        div {
            crates.forEach { crate ->
                div("box") {
                    h3 { +crate.name }
                    crate.image?.let {
                        img {
                            src = it
                            alt = crate.name ?: "Crate Image"
                            width = "200"
                        }
                    }
                    p { +crate.description.orEmpty() }
                }
            }
        }
        insert(cratesListContent)
    }
}

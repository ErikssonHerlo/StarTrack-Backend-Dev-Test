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
import stsa.kotlin_htmx.external.dto.KeyDto
import stsa.kotlin_htmx.api.ApiResponse

class KeysPage {
    private val client = HttpClient()
    private val baseUrl = System.getenv("BASE_URL") ?: "http://localhost:8080"

    suspend fun render(ctx: RoutingContext) {
        val response: HttpResponse = client.get("$baseUrl/api/v1/keys") {
            header(HttpHeaders.Authorization, "Bearer Token")
        }
        val json = Json.decodeFromString<ApiResponse<List<KeyDto>>>(response.bodyAsText())

        ctx.call.respondHtmlTemplate(MainTemplate(template = KeysTemplate(json.data ?: emptyList()), "Keys List")) {
            mainSectionTemplate {
                keysListContent {}
            }
        }
    }
}

class KeysTemplate(private val keys: List<KeyDto>) : Template<FlowContent> {
    val keysListContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 { +"Lista de Llaves" }
        div {
            keys.forEach { key ->
                div("box") {
                    h3 { +key.name }
                    key.image?.let {
                        img {
                            src = it
                            alt = key.name
                            width = "200"
                        }
                    }
                    p { +key.description.orEmpty() }
                    if (key.crates.isNotEmpty()) {
                        ul {
                            h4 { +"Crates:" }
                            key.crates.forEach { crate ->
                                li { +"${crate.name}" }
                            }
                        }
                    }
                }
            }
        }
        insert(keysListContent)
    }
}

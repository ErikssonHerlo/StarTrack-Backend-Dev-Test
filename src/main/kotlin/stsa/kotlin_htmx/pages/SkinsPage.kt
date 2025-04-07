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
import stsa.kotlin_htmx.external.dto.SkinDto
import stsa.kotlin_htmx.api.ApiResponse
import java.io.File

class SkinsPage {

    private val client = HttpClient()
    private val baseUrl: String by lazy {
        val envFile = File(".env.local")
        val props = envFile.readLines()
            .mapNotNull { line -> line.split("=").takeIf { it.size == 2 } }
            .associate { it[0].trim() to it[1].trim() }
        props["BASE_URL"] ?: "http://localhost:8080"
    }

    suspend fun render(ctx: RoutingContext) {
        val response: HttpResponse = client.get("$baseUrl/api/v1/skins")
        val json = Json.decodeFromString<ApiResponse<List<SkinDto>>>(response.bodyAsText())

        ctx.call.respondHtmlTemplate(MainTemplate(template = SkinsTemplate(json.data ?: emptyList()), "Skins List")) {
            mainSectionTemplate {
                skinsListContent {}
            }
        }
    }
}

class SkinsTemplate(private val skins: List<SkinDto>) : Template<FlowContent> {
    val skinsListContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 { +"List of Skins" }
        div {
            skins.forEach { skin ->
                div("box") {
                    h3 { +skin.name }

                    skin.image?.let {
                        img {
                            src = it
                            alt = skin.name
                            width = "200"
                        }
                    }

                    skin.description?.let {
                        p { +it }
                    }

                    skin.team?.let {
                        p { +"Team: ${it.name}" }
                    }

                    if (skin.crates.isNotEmpty()) {
                        ul {
                            p { +"Crates:" }
                            skin.crates.forEach { crate ->
                                li {
                                    +"${crate.name}"
                                }
                            }
                        }
                    }
                }
            }
        }
        insert(skinsListContent)
    }
}

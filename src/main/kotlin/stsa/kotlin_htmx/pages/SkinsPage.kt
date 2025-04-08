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
import kotlinx.serialization.encodeToString
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
        val search = ctx.call.request.queryParameters["search"]
        val url = if (!search.isNullOrBlank())
            "$baseUrl/api/v1/skins/search?search=$search"
        else
            "$baseUrl/api/v1/skins"

        val response: HttpResponse = client.get(url)
        val json = Json.decodeFromString<ApiResponse<List<SkinDto>>>(response.bodyAsText())
        val jsonString = Json.encodeToString(mapOf("data" to (json.data ?: emptyList())))

        val isHtmx = ctx.call.request.headers["HX-Request"] == "true"

        if (isHtmx) {
            ctx.call.respondHtml {
                body {
                    for (skin in json.data.orEmpty()) {
                        div("box") {
                            h3 { +skin.name }
                            skin.image?.let {
                                img {
                                    src = it
                                    alt = skin.name
                                    width = "200"
                                }
                            }
                            skin.description?.let { p { +it } }
                            skin.team?.let { p { +"Team: ${it.name}" } }
                            if (skin.crates.isNotEmpty()) {
                                ul {
                                    p { +"Crates:" }
                                    skin.crates.forEach { crate -> li { +crate.name } }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            ctx.call.respondHtmlTemplate(
                MainTemplate(
                    template = SkinsTemplate(json.data ?: emptyList(), jsonString),
                    pageTitle = "Skins List"
                )
            ) {
                mainSectionTemplate {
                    skinsListContent {}
                }
            }
        }
    }
}

class SkinsTemplate(private val skins: List<SkinDto>, private val jsonData: String) : Template<FlowContent> {
    val skinsListContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        h2 { +"List of Skins" }

        form {
            attributes["hx-get"] = "/skins"
            attributes["hx-target"] = "#skinsResults"
            attributes["hx-swap"] = "innerHTML"
            input {
                type = InputType.text
                name = "search"
                placeholder = "Search skins"
            }
            button {
                type = ButtonType.submit
                +"Search"
            }
        }

        button {
            attributes["data-json"] = jsonData.replace("\"", "&quot;")
            onClick = "exportSkinsAsXml(this)"
            +"Export as XML"
        }

        script {
            unsafe {
                +"""
                    function exportSkinsAsXml(button) {
                        const raw = button.getAttribute("data-json");
                        const parsed = JSON.parse(raw.replace(/&quot;/g, '"'));
                        
                        fetch("/api/v1/skins/export/XML", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                                "Accept": "application/json"
                            },
                            body: JSON.stringify(parsed)
                        })
                        .then(response => response.text())
                        .then(xml => {
                            const blob = new Blob([xml], { type: 'application/xml' });
                            const link = document.createElement('a');
                            link.href = URL.createObjectURL(blob);
                            link.download = 'skins.xml';
                            document.body.appendChild(link);
                            link.click();
                            document.body.removeChild(link);
                        });
                    }
                """.trimIndent()
            }
        }

        div {
            id = "skinsResults"
            insert(skinsListContent)
            for (skin in skins) {
                div("box") {
                    h3 { +skin.name }
                    skin.image?.let {
                        img {
                            src = it
                            alt = skin.name
                            width = "200"
                        }
                    }
                    skin.description?.let { p { +it } }
                    skin.team?.let { p { +"Team: ${it.name}" } }
                    if (skin.crates.isNotEmpty()) {
                        ul {
                            p { +"Crates:" }
                            skin.crates.forEach { crate -> li { +crate.name } }
                        }
                    }
                }
            }
        }
    }
}

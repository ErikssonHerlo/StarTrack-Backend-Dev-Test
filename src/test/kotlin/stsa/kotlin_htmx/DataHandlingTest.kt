package stsa.kotlin_htmx

import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertEquals

class CratesExportTest {
    private val mockXmlResponse = """
        <crates>
            <crate>
                <id>crate-1210</id>
                <name>Paquete de regalo</name>
                <description>Cuando se use, un jugador aleatorio de la partida recibirá un objeto aleatorio como regalo de tu parte.</description>
                <image>https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/weapon_cases/gift1player_png.png</image>
            </crate>
        </crates>
    """.trimIndent()

    private val mockClient = io.ktor.client.HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        engine {
            addHandler { request ->
                if (request.method == HttpMethod.Post && request.url.encodedPath == "/api/v1/crates/export/XML") {
                    respond(
                        content = mockXmlResponse,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "text/xml; charset=UTF-8")
                    )
                } else {
                    respondBadRequest()
                }
            }
        }
    }

    @Test
    fun shouldExportCratesToXml() = runBlocking {
        val payload = """
            {
              "data": [
                {
                  "id": "crate-1210",
                  "name": "Paquete de regalo",
                  "description": "Cuando se use, un jugador aleatorio de la partida recibirá un objeto aleatorio como regalo de tu parte.",
                  "image": "https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/weapon_cases/gift1player_png.png"
                }
              ]
            }
        """.trimIndent()

        val response = mockClient.post("/api/v1/crates/export/XML") {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Xml.withCharset(Charsets.UTF_8), response.contentType())

        val xml = response.bodyAsText()
        val db = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = db.parse(InputSource(StringReader(xml)))
        doc.documentElement.normalize()
        assertEquals("crates", doc.documentElement.nodeName)
    }
}

package stsa.kotlin_htmx

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Test
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertEquals

class DataHandlingTest {

    @Test
    fun shouldExportAgentsToXml() = testApplication {
        application { module() }

        val payload = """
        {
            "data": [
                {
                    "id": "agent-4613",
                    "name": "Sr. Bloody Darryl El Preparado | Los Profesionales",
                    "description": "Antes de convertirse en el líder de la banda de ladrones conocida como \\\"Los Profesionales\\\", Sir Bloody Darryl era conocido simplemente como Bloody Darryl. Todavía sigue siendo el psicópata amistoso de tu vecindario en todo el sentido de la palabra. De hecho no es australiano, según algunos australianos. \\n\\n<i>Solo les voy a dar un toque de extravagancia</i>",
                    "team": {
                        "id": "terrorists",
                        "name": "Terrorista"
                    },
                    "image": "https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/characters/customplayer_tm_professional_varf5_png.png"
                }
            ]
        }
        """.trimIndent()

        val response = client.post("/api/v1/agents/export/XML") {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Xml.withCharset(Charsets.UTF_8), response.contentType())

        val xml = response.bodyAsText()
        val db = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        db.parse(InputSource(StringReader(xml))) // valida estructura XML
    }
}

package stsa.kotlin_htmx

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.testApplication
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
        // Example payload for agents export; it must match the expected SkinExportRequestDto or AgentExportRequestDto structure.
        val payload = """
            {
              "data": [
                {
                  "id": "agent-4613",
                  "name": "Sr. Bloody Darryl El Preparado | Los Profesionales",
                  "description": "Antes de convertirse en el líder de la banda de ladrones...",
                  "team": {
                    "id": "terrorists",
                    "name": "Terrorista"
                  },
                  "image": "https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/characters/customplayer_tm_professional_varf5_png.png",
                  "crates": []
                }
              ]
            }
        """.trimIndent()

        // Test the export endpoint for agents
        val response = client.post("/api/v1/agents/export/XML") {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Text.Xml.withCharset(Charsets.UTF_8), response.contentType())

        val xml = response.bodyAsText()
        // Validate XML structure by parsing it
        val db = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = db.parse(InputSource(StringReader(xml)))
        doc.documentElement.normalize()
        // Check that the root element is "data"
        assertEquals("agents", doc.documentElement.nodeName)
    }
}

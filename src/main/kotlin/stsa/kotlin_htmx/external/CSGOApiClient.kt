package stsa.kotlin_htmx.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import stsa.kotlin_htmx.external.dto.SkinDto
import stsa.kotlin_htmx.config.AppConstants
import stsa.kotlin_htmx.external.dto.AgentDto
import stsa.kotlin_htmx.external.dto.CrateDto
import stsa.kotlin_htmx.external.dto.KeyDto


class CSGOApiClient : CSGOApiClientInterface {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json() // Use default JSON configuration
        }
    }

    private var json: Json = Json { ignoreUnknownKeys = true }

    override suspend fun getSkins(): List<SkinDto> {
        val url = "${AppConstants.BASE_URL}/skins.json"
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        // Lee el cuerpo como texto, ya que la API responde con "text/plain"
        val responseText = response.bodyAsText()
        // Decodifica el texto a la lista de SkinDto usando Json
        return json.decodeFromString(responseText)
    }

    override suspend fun getAgents(): List<AgentDto> {
        val url = "${AppConstants.BASE_URL}/agents.json"
        return client.get(url).body()
    }

    override suspend fun getCrates(): List<CrateDto> {
        val url = "${AppConstants.BASE_URL}/crates.json"
        return client.get(url).body()
    }

    override suspend fun getKeys(): List<KeyDto> {
        val url = "${AppConstants.BASE_URL}/keys.json"
        return client.get(url).body()
    }
}
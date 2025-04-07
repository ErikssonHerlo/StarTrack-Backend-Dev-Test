package stsa.kotlin_htmx.external

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import stsa.kotlin_htmx.config.AppConstants
import stsa.kotlin_htmx.external.dto.*


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
        // Read the response body as text
        val responseText = response.bodyAsText()
        // Decode the JSON response into a list of SkinDto
        return json.decodeFromString(responseText)
    }

    override suspend fun getAgents(): List<AgentDto> {
        val url = "${AppConstants.BASE_URL}/agents.json"
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        // Read the response body as text
        val responseText = response.bodyAsText()
        // Decode the JSON response into a list of SkinDto
        return json.decodeFromString(responseText)
    }

    override suspend fun getCrates(): List<CrateDto> {
        val url = "${AppConstants.BASE_URL}/crates.json"
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        // Read the response body as text
        val responseText = response.bodyAsText()
        // Decode the JSON response into a list of SkinDto
        return json.decodeFromString(responseText)
    }

    override suspend fun getKeys(): List<KeyDto> {
        val url = "${AppConstants.BASE_URL}/keys.json"
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        // Read the response body as text
        val responseText = response.bodyAsText()
        // Decode the JSON response into a list of SkinDto
        return json.decodeFromString(responseText)
    }
}
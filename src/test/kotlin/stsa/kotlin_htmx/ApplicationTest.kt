package stsa.kotlin_htmx

import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApplicationTest {

    private val mockResponse = """
        {
            "code": 200,
            "status": "OK",
            "message": "Success",
            "data": [
                {
                    "mockup":           "mocked data"
                }
            ],
            "errors": null
        }
    """.trimIndent()

    @Test
    fun testMockedGetSkinsEndpoint() = runBlocking {
        val client = io.ktor.client.HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/api/v1/skins" -> {
                            respond(
                                content = mockResponse,
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        else -> error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        val response: HttpResponse = client.get("/api/v1/skins")
        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        assertNotNull(responseBody)
        println("Response: $responseBody")
    }

    @Test
    fun testMockedGetAgentsEndpoint() = runBlocking {
        val client = io.ktor.client.HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/api/v1/agents" -> {
                            respond(
                                content = mockResponse,
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        else -> error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        val response: HttpResponse = client.get("/api/v1/agents")
        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        assertNotNull(responseBody)
        println("Response: $responseBody")
    }

    @Test
    fun testMockedGetCratesEndpoint() = runBlocking {
        val client = io.ktor.client.HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/api/v1/crates" -> {
                            respond(
                                content = mockResponse,
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        else -> error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        val response: HttpResponse = client.get("/api/v1/crates")
        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        assertNotNull(responseBody)
        println("Response: $responseBody")
    }




    @Test
    fun testMockedGetKeysUnauthorized() = runBlocking {
        val client = io.ktor.client.HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/api/v1/keys" -> {
                            respond(
                                content = """
                                {
                                    "code": 401,
                                    "status": "ERROR",
                                    "message": "Unauthorized",
                                    "data": null,
                                    "errors": ["Missing or invalid token"]
                                }
                            """.trimIndent(),
                                status = HttpStatusCode.Unauthorized,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }

                        else -> error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        val response: HttpResponse = client.get("/api/v1/keys") // sin token
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        println("Unauthorized Response: ${response.bodyAsText()}")
    }

    @Test
    fun testMockedGetKeysAuthorized() = runBlocking {
        val client = io.ktor.client.HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/api/v1/keys" -> {
                            val authHeader = request.headers[HttpHeaders.Authorization]
                            if (authHeader == "Bearer Token") {
                                respond(
                                    content = """
                                    {
                                        "code": 200,
                                        "status": "OK",
                                        "message": "Success",
                                        "data": [
                                            {
                                                "id": "key-001",
                                                "name": "Llave clásica",
                                                "description": "Abre las cajas clásicas",
                                                "crates": [
                                                    {
                                                        "id": "crate-1210",
                                                        "name": "Paquete de regalo",
                                                        "image": "https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/weapon_cases/gift1player_png.png"
                                                    }
                                                ],
                                                "image": "https://raw.githubusercontent.com/ByMykel/counter-strike-image-tracker/main/static/panorama/images/econ/default_generated/weapon_key_key_gold_png.png"
                                            }
                                        ],
                                        "errors": null
                                    }
                                """.trimIndent(),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                                )
                            } else {
                                respond(
                                    content = """{"message":"Unauthorized"}""",
                                    status = HttpStatusCode.Unauthorized
                                )
                            }
                        }

                        else -> error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        val response: HttpResponse = client.get("/api/v1/keys") {
            header(HttpHeaders.Authorization, "Bearer Token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        println("Authorized Response: ${response.bodyAsText()}")
    }



}

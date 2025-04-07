package stsa.kotlin_htmx

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testGetSkins() = testApplication {
        application { module() }

        val response = client.get("/api/v1/skins")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetAgents() = testApplication {
        application { module() }

        val response = client.get("/api/v1/agents")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetCrates() = testApplication {
        application { module() }

        val response = client.get("/api/v1/crates")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testUnauthorizedKeys() = testApplication {
        application { module() }

        val response = client.get("/api/v1/keys")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun testAuthorizedKeys() = testApplication {
        application { module() }

        val response = client.get("/api/v1/keys") {
            header("Authorization", "Bearer secret")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
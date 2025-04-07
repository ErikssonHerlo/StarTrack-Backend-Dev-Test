package stsa.kotlin_htmx.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureApiRoutes() {
    routing {
        get("/skins") {
            call.respond(HttpStatusCode.OK, "Skins endpoint")
        }
        get("/agents") {
            call.respond(HttpStatusCode.OK, "Agents endpoint")
        }
        get("/crates") {
            call.respond(HttpStatusCode.OK, "Crates endpoint")
        }
        get("/keys") {
            val authHeader = call.request.headers["Authorization"]
            if (authHeader != "Bearer secret") {
                call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
            } else {
                call.respond(HttpStatusCode.OK, "Keys endpoint")
            }
        }
        get("/xml") {
            call.respondText(
                text = "<data><message>XML response</message></data>",
                contentType = ContentType.Text.Xml.withCharset(Charsets.UTF_8)
            )
        }
    }
}

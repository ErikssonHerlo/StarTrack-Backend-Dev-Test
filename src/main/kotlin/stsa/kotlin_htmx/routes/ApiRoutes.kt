package stsa.kotlin_htmx.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import stsa.kotlin_htmx.api.ApiResponse
import stsa.kotlin_htmx.external.CSGOApiClient
import stsa.kotlin_htmx.external.dto.*
import stsa.kotlin_htmx.repositories.AgentRepository
import stsa.kotlin_htmx.repositories.CrateRepository
import stsa.kotlin_htmx.repositories.KeyRepository
import stsa.kotlin_htmx.repositories.SkinRepository
import stsa.kotlin_htmx.services.*

fun Application.configureApiRoutes() {

    val skinService = SkinService(SkinRepository())
    val agentService = AgentService(AgentRepository())
    val crateService = CrateService(CrateRepository())
    val keyService = KeyService(KeyRepository())

    routing {
        staticResources("static", "static")

        route("/api/v1/skins") {
            get {
                val skinsDto = skinService.getAllSkins()
                call.respond(HttpStatusCode.OK, ApiResponse.success(data = skinsDto))
            }

            get("/search") {
                val search = call.request.queryParameters["search"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ApiResponse.error<Unit>(message = "Missing search parameter"))
                val skinsDto = skinService.getSkinsByName(search)
                call.respond(HttpStatusCode.OK, ApiResponse.success(data = skinsDto))
            }

            post("/export/XML") {
                // Receive the JSON payload as ExportRequest
                val skinExportRequestDto = call.receive<SkinExportRequestDto>()
                val skins: List<SkinDto> = skinExportRequestDto.data
                val xmlOutput = skinService.skinsExportToXmlFromRequest(skins)
                call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"skins_export.xml\"")
                call.respondText(xmlOutput, ContentType.Text.Xml)
            }
        }

        route("/api/v1/agents") {
            get {
                val agentsDto = agentService.getAllAgents()
                call.respond(HttpStatusCode.OK, ApiResponse.success(data = agentsDto))
            }

            // POST endpoint to export agents to XML
            post("/export/XML") {
                // Receive the JSON payload containing a list of AgentDto
                val exportRequest = call.receive<AgentExportRequestDto>()
                val agents: List<stsa.kotlin_htmx.external.dto.AgentDto> = exportRequest.data
                val xmlOutput = agentService.agentsExportToXmlFromRequest(agents)
                // Force the download as an XML file by setting Content-Disposition header
                call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"agents_export.xml\"")
                call.respondText(xmlOutput, ContentType.Text.Xml)
            }
        }
        route("/api/v1/crates") {
            get {
                val cratesDto = crateService.getAllCrates()
                call.respond(HttpStatusCode.OK, ApiResponse.success(data = cratesDto))
            }
            post("/export/XML") {
                val crateExportRequestDto = call.receive<CrateExportRequestDto>()
                // Dado que el DTO ya es List<CrateDto>, se utiliza directamente:
                val xmlOutput = crateService.cratesExportToXmlFromRequest(crateExportRequestDto.data)
                call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"crates_export.xml\"")
                call.respondText(xmlOutput, ContentType.Text.Xml)
            }
        }
        route("/api/v1/keys") {

            get {
                // Protect the keys endpoint with authentication
                val authHeader = call.request.headers["Authorization"]
                if (authHeader == null || authHeader != "Bearer Token") {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse.error<Unit>(
                            message = "Unauthorized",
                            errors = listOf("Missing or invalid token"),
                            code = 401
                        )
                    )
                } else {
                    val keysDto = keyService.getAllKeys()
                    call.respond(HttpStatusCode.OK, ApiResponse.success(data = keysDto))
                }
            }

            post("/export/XML") {
                val keyExportRequestDto = call.receive<KeyExportRequestDto>()
                val xmlOutput = KeyService(stsa.kotlin_htmx.repositories.KeyRepository())
                    .keysExportToXmlFromRequest(keyExportRequestDto.data)
                call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"keys_export.xml\"")
                call.respondText(xmlOutput, ContentType.Text.Xml)
            }
        }
    }
}


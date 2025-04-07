package stsa.kotlin_htmx

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import kotlinx.coroutines.runBlocking
import stsa.kotlin_htmx.plugins.configureHTTP
import stsa.kotlin_htmx.plugins.configureMonitoring
import stsa.kotlin_htmx.plugins.configureRouting
import stsa.kotlin_htmx.routes.configureApiRoutes
import stsa.kotlin_htmx.routes.configurePageRoutes
import stsa.kotlin_htmx.config.DatabaseConfig
import java.io.File

import stsa.kotlin_htmx.external.CSGOApiClient
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.SkinRepository
import stsa.kotlin_htmx.repositories.AgentRepository
import stsa.kotlin_htmx.repositories.CrateRepository
import stsa.kotlin_htmx.repositories.KeyRepository
import stsa.kotlin_htmx.services.*


data class ApplicationConfig(
    val lookupApiKey: String,
    val apiBaseUrl: String
) {

    companion object {
        fun load(): ApplicationConfig {
            System.setProperty("io.ktor.development", "true")

            fun Map<String, String>.envOrLookup(key: String): String {
                return System.getenv(key) ?: this[key]!!
            }

            val envVars: Map<String, String> = envFile().let { envFile ->
                if (envFile.exists()) {
                    envFile.readLines()
                        .map { it.split("=") }
                        .filter { it.size == 2 }
                        .associate { it.first().trim() to it.last().trim() }
                } else emptyMap()
            }

            return ApplicationConfig(
                lookupApiKey = envVars.envOrLookup("LOOKUP_API_KEY"),
                apiBaseUrl = envVars.envOrLookup("BASE_URL")
            )
        }

    }
}

fun envFile(): File {
    // I don't really recommend having this default env file, but do it now to ease testing of example app
    // Settings in ENV will override file always
    return listOf(".env.local", ".env.default").map { File(it) }.first { it.exists() }
}

/**
 * This function is called when the application starts.
 * It loads the data for CSGO skins, agents, crates, and keys.
 */
suspend fun loadCSGOData() {
    // Initialize Dependencies Injection
    val apiClient: CSGOApiClientInterface = CSGOApiClient()
    val csgoService: CSGOService = CSGOService(
        apiClient,
        SkinRepository(),
        CrateRepository(),
        AgentRepository(),
        KeyRepository()
    )
    // Load Skins data
    csgoService.loadSkinsData()

    // Load Agents data
    csgoService.loadAgentsData()

    // Load Crates data
    csgoService.loadCratesData()

    // Load Keys data
    csgoService.loadKeysData()

}


fun main() {
    // Configure the database connection & applied migrations
    DatabaseConfig.init()

    runBlocking {
        loadCSGOData()
    }

    // Have to do this before the rest of the loading of KTor. I guess it's because it does something fancy
    // with the classloader to be able to do hot reload.
    if (envFile().readText().contains("KTOR_DEVELOPMENT=true")) System.setProperty(
        "io.ktor.development",
        "true"
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureHTTP()
    install(ContentNegotiation) {
        json() // default configuration; you can customize if needed
    }
    configureMonitoring()
    configureRouting()
    configureApiRoutes()
    install(Compression)

    // Manual dependency injection :) Usually smart to find a separate place to do this from KTor
    val config = ApplicationConfig.load()

    // Load pages
    configurePageRoutes()
}

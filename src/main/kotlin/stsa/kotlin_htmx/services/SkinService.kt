package stsa.kotlin_htmx.services

import org.jetbrains.exposed.sql.transactions.transaction
import stsa.kotlin_htmx.domain.models.Skin
import stsa.kotlin_htmx.external.CSGOApiClientInterface
import stsa.kotlin_htmx.repositories.SkinRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class SkinService(
    private val apiClient: CSGOApiClientInterface,
    private val repository: SkinRepository
) {
    private val logger = LoggerFactory.getLogger(SkinService::class.java)
    /**
     * Loads skins data from the API and saves it to the database.
     * This function fetches the skins data from the API, transforms it into
     * domain models, and then persists it in the database.
     */
    suspend fun loadSkinsData() {

        try {
            val count = repository.count()
            if (count > 0) {
                logger.info("La tabla de skins ya contiene datos (count = $count). Se omite la carga de datos.")
                return
            }
            logger.info("La tabla de skins está vacía. Iniciando la carga de datos...")

            // Get Skins list from API as DTOs
            val skinsDto = apiClient.getSkins()

            // Transform each DTO into a domain model
            val skins: List<Skin> = skinsDto.map { dto ->
                Skin(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    team = Json.encodeToString(dto.team),
                    image = dto.image,
                    // Convert the list of crates to a comma-separated string
                    crates = Json.encodeToString(dto.crates)
                )
            }

            // Insert each skin into the database using the repository & transaction block
            transaction {
                skins.forEach { skin ->
                    repository.save(skin)
                    logger.info("Insertada skin con id: ${skin.id}")
                }
            }
        } catch (e: Exception) {
            logger.error("Error al cargar los datos de skins: ${e.message}", e)
        }
    }
}

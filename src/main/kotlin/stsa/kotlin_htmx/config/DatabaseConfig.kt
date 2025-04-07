package stsa.kotlin_htmx.config

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    fun init() {
        // Leer variables de entorno o usar valores por defecto.
        val jdbcUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/CSGO"
        val dbUser = System.getenv("DB_USER") ?: "postgres"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "postgres"

        // Configurar Flyway para aplicar las migraciones.
        val flyway = Flyway.configure()
            .dataSource(jdbcUrl, dbUser, dbPassword)
            .locations("classpath:db/migration")  // Por defecto, Flyway busca en src/main/resources/db/migration
            .load()

        // Ejecutar las migraciones pendientes.
        flyway.migrate()

        // Conectar a la base de datos usando Exposed.
        Database.connect(
            url = jdbcUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    }
}

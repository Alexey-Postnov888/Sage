package ru.sage.registryservice.data.exposed.generator

import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Configuration
import ru.sage.registryservice.data.exposed.table.PluginCapabilitiesTable
import ru.sage.registryservice.data.exposed.table.PluginInstancesTable
import ru.sage.registryservice.data.exposed.table.PluginsTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class MigrationConfig

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun main(args: Array<String>) {
    SpringApplicationBuilder(MigrationConfig::class.java)
        .web(WebApplicationType.NONE)
        .run()
        .use { context ->
            val env = context.environment
            val db = Database.connect(
                url = env.getRequiredProperty("spring.datasource.url"),
                user = env.getRequiredProperty("spring.datasource.username"),
                password = env.getRequiredProperty("spring.datasource.password"),
            )

            val version = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HHmmss"))

            transaction(db) {
                MigrationUtils.generateMigrationScript(
                    PluginsTable, PluginInstancesTable, PluginCapabilitiesTable,
                    scriptDirectory = args.firstOrNull() ?: "src/main/resources/db/migration",
                    scriptName = "V${version}__migration",
                )
            }
        }
}
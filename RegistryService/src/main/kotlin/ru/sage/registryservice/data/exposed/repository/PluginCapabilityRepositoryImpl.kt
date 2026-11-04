package ru.sage.registryservice.data.exposed.repository

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Repository
import ru.sage.registryservice.data.exposed.mapper.toPluginCapability
import ru.sage.registryservice.data.exposed.table.PluginCapabilitiesTable
import ru.sage.registryservice.domain.model.PluginCapability
import ru.sage.registryservice.domain.repository.PluginCapabilityRepository

@Repository
class PluginCapabilityRepositoryImpl : PluginCapabilityRepository {
    override fun findById(pluginId: String, id: String): PluginCapability? =
        PluginCapabilitiesTable
            .selectAll()
            .where {
                (PluginCapabilitiesTable.pluginId eq pluginId) and
                        (PluginCapabilitiesTable.id eq id)
            }
            .singleOrNull()
            ?.toPluginCapability()

    override fun findByPluginId(pluginId: String): List<PluginCapability> =
        PluginCapabilitiesTable
            .selectAll()
            .where { PluginCapabilitiesTable.pluginId eq pluginId }
            .map { it.toPluginCapability() }

    override fun saveAll(capabilities: List<PluginCapability>) {
        if (capabilities.isEmpty()) return

        PluginCapabilitiesTable.batchUpsert(
            data = capabilities,
            keys = arrayOf(
                PluginCapabilitiesTable.pluginId,
                PluginCapabilitiesTable.id
            )
        ) {
            this[PluginCapabilitiesTable.id] = it.id
            this[PluginCapabilitiesTable.pluginId] = it.pluginId
            this[PluginCapabilitiesTable.description] = it.description
        }
    }

    override fun syncWithManifest(
        pluginId: String,
        capabilities: List<PluginCapability>
    ) {
        PluginCapabilitiesTable.deleteWhere {
            PluginCapabilitiesTable.pluginId eq pluginId
        }

        if (capabilities.isEmpty()) return

       saveAll(capabilities)
    }
}
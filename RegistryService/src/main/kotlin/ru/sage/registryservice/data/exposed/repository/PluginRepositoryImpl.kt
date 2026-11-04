package ru.sage.registryservice.data.exposed.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository
import ru.sage.registryservice.data.exposed.mapper.toPlugin
import ru.sage.registryservice.data.exposed.table.PluginsTable
import ru.sage.registryservice.domain.model.Plugin
import ru.sage.registryservice.domain.repository.PluginRepository
import java.time.LocalDateTime

@Repository
class PluginRepositoryImpl : PluginRepository {
    override fun findById(id: String): Plugin? =
        PluginsTable
            .selectAll()
            .where { PluginsTable.id eq id }
            .singleOrNull()
            ?.toPlugin()

    override fun save(plugin: Plugin): Plugin {
        val now = LocalDateTime.now()

        PluginsTable.upsert(
            PluginsTable.id,
            onUpdate = {
                it[PluginsTable.name] = plugin.name
                it[PluginsTable.description] = plugin.description
                it[PluginsTable.version] = plugin.version
                it[PluginsTable.manifestVersion] = plugin.manifestVersion
                it[PluginsTable.updatedAt] = now
            }
        ) {
            it[id] = plugin.id
            it[name] = plugin.name
            it[description] = plugin.description
            it[version] = plugin.version
            it[manifestVersion] = plugin.manifestVersion
            it[createdAt] = now
            it[updatedAt] = now
        }

        return findById(plugin.id)!!
    }

    override fun deleteById(id: String) {
        PluginsTable.deleteWhere { PluginsTable.id eq id }
    }
}
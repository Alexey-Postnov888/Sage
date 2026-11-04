package ru.sage.registryservice.data.exposed.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import ru.sage.registryservice.data.exposed.table.PluginCapabilitiesTable
import ru.sage.registryservice.domain.model.PluginCapability

fun ResultRow.toPluginCapability(): PluginCapability = PluginCapability(
    id = this[PluginCapabilitiesTable.id],
    pluginId = this[PluginCapabilitiesTable.pluginId],
    description = this[PluginCapabilitiesTable.description]
)
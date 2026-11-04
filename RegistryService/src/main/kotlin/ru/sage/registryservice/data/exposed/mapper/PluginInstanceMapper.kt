package ru.sage.registryservice.data.exposed.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import ru.sage.registryservice.data.exposed.table.PluginInstancesTable
import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.domain.model.PluginInstanceStatus

fun ResultRow.toPluginInstance(): PluginInstance = PluginInstance(
    id = this[PluginInstancesTable.id],
    endpoint = this[PluginInstancesTable.endpoint],
    status = PluginInstanceStatus.valueOf(this[PluginInstancesTable.status]),
    pluginId = this[PluginInstancesTable.pluginId],
    lastHeartbeatAt = this[PluginInstancesTable.lastHeartbeatAt],
)
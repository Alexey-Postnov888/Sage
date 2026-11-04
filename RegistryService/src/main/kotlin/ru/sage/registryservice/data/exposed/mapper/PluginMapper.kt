package ru.sage.registryservice.data.exposed.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import ru.sage.registryservice.data.exposed.table.PluginsTable
import ru.sage.registryservice.domain.model.Plugin

fun ResultRow.toPlugin(): Plugin = Plugin(
    id = this[PluginsTable.id],
    name = this[PluginsTable.name],
    description = this[PluginsTable.description],
    version = this[PluginsTable.version],
    manifestVersion = this[PluginsTable.manifestVersion],
)
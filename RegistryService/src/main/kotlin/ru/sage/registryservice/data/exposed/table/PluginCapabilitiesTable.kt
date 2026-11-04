package ru.sage.registryservice.data.exposed.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object PluginCapabilitiesTable : Table("plugin_capabilities") {
    val id = varchar("id", 255)
    val pluginId = reference(
        name = "plugin_id",
        refColumn = PluginsTable.id,
        onDelete = ReferenceOption.CASCADE
    )
    val description = text("description")

    override val primaryKey = PrimaryKey(pluginId, id)
}

package ru.sage.registryservice.data.exposed.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.javatime.datetime

object PluginInstancesTable : Table("plugin_instances") {
    val id = javaUUID("id").autoGenerate()
    val endpoint = text("endpoint")
    val status = varchar("status", 255)
    val pluginId = reference(
        name = "plugin_id",
        refColumn = PluginsTable.id,
        onDelete = ReferenceOption.CASCADE
    )
    val lastHeartbeatAt = datetime("last_heartbeat_at").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
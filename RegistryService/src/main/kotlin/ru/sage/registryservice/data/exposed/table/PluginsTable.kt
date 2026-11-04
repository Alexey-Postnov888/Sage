package ru.sage.registryservice.data.exposed.table

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.datetime

object PluginsTable : Table("plugins") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val description = text("description")
    val version = varchar("version", 255)
    val manifestVersion = varchar("manifest_version", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
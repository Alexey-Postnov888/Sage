package ru.sage.registryservice.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class PluginInstance(
    val id: UUID = UUID.randomUUID(),
    val endpoint: String,
    val status: PluginInstanceStatus,
    val pluginId: String,
    val lastHeartbeatAt: LocalDateTime? = null,
)

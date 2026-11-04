package ru.sage.registryservice.presentation.model

import ru.sage.registryservice.domain.model.PluginInstanceStatus
import java.util.UUID

data class PluginInstanceResponse(
    val id: UUID,
    val pluginId: String,
    val endpoint: String,
    val status: PluginInstanceStatus
)
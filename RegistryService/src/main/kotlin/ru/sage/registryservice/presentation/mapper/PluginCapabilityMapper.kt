package ru.sage.registryservice.presentation.mapper

import ru.sage.registryservice.domain.model.PluginCapability
import ru.sage.registryservice.presentation.model.PluginCapabilityResponse

fun PluginCapability.toPresentation(): PluginCapabilityResponse = PluginCapabilityResponse(
    id = id,
    pluginId = pluginId,
    description = description,
)
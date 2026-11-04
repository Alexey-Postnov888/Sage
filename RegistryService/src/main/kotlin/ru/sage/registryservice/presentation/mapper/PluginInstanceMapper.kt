package ru.sage.registryservice.presentation.mapper

import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.presentation.model.PluginInstanceResponse

fun PluginInstance.toPresentation(): PluginInstanceResponse =
    PluginInstanceResponse(
        id = id,
        pluginId = pluginId,
        endpoint = endpoint,
        status = status,
    )
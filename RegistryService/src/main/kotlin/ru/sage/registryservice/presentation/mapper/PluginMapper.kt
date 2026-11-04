package ru.sage.registryservice.presentation.mapper

import ru.sage.registryservice.domain.model.Plugin
import ru.sage.registryservice.presentation.model.PluginResponse

fun Plugin.toPresentation(): PluginResponse = PluginResponse(
    id = id,
    name = name,
    description = description,
    version = version,
    manifestVersion = manifestVersion
)
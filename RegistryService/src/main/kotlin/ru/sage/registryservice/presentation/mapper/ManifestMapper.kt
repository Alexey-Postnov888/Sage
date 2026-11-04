package ru.sage.registryservice.presentation.mapper

import ru.sage.registryservice.domain.model.Plugin
import ru.sage.registryservice.domain.model.PluginCapability
import ru.sage.registryservice.domain.model.PluginRegistration
import ru.sage.registryservice.presentation.model.ManifestDto

fun ManifestDto.toPluginRegistration(): PluginRegistration {
    val plugin = Plugin(
        id = info.id,
        name = info.name,
        description = info.description,
        version = info.version,
        manifestVersion = manifestVersion,
    )

    val capabilities = spec.capabilities.map {
        PluginCapability(
            id = it.id,
            pluginId = info.id,
            description = it.description,
        )
    }

    return PluginRegistration(
        plugin = plugin,
        capabilities = capabilities,
        endpoint = spec.endpoint
    )
}
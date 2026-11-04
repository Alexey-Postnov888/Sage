package ru.sage.registryservice.presentation.model

data class ManifestDto(
    val manifestVersion: String,
    val type: String,
    val info: PluginInfoDto,
    val spec: PluginSpecDto
)

data class PluginInfoDto(
    val id: String,
    val version: String,
    val name: String,
    val description: String
)

data class PluginSpecDto(
    val endpoint: String,
    val capabilities: List<PluginCapabilityDto>
)

data class PluginCapabilityDto(
    val id: String,
    val description: String
)

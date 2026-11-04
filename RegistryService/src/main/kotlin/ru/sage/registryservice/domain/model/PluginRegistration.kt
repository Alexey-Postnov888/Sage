package ru.sage.registryservice.domain.model

data class PluginRegistration(
    val plugin: Plugin,
    val capabilities: List<PluginCapability>,
    val endpoint: String
)

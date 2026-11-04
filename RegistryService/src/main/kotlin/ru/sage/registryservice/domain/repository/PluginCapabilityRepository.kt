package ru.sage.registryservice.domain.repository

import ru.sage.registryservice.domain.model.PluginCapability

interface PluginCapabilityRepository {
    fun findById(pluginId: String, id: String): PluginCapability?
    fun findByPluginId(pluginId: String): List<PluginCapability>
    fun saveAll(capabilities: List<PluginCapability>)
    fun syncWithManifest(pluginId: String, capabilities: List<PluginCapability>)
}
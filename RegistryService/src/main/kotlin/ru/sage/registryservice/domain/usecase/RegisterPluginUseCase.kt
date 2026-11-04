package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.model.PluginRegistration
import ru.sage.registryservice.domain.repository.PluginCapabilityRepository
import ru.sage.registryservice.domain.repository.PluginInstanceRepository
import ru.sage.registryservice.domain.repository.PluginRepository

interface RegisterPluginUseCase {
    operator fun invoke(pluginRegistration: PluginRegistration): PluginInstance
}

@Service
class RegisterPluginUseCaseImpl(
    private val pluginRepository: PluginRepository,
    private val pluginCapabilityRepository: PluginCapabilityRepository,
    private val pluginInstanceRepository: PluginInstanceRepository
) : RegisterPluginUseCase {
    override fun invoke(pluginRegistration: PluginRegistration): PluginInstance =
        transaction {
            val plugin = pluginRepository.save(
                pluginRegistration.plugin
            )

            pluginCapabilityRepository.syncWithManifest(
                pluginId = plugin.id,
                capabilities = pluginRegistration.capabilities
            )

            pluginInstanceRepository.save(
                pluginInstance = PluginInstance(
                    endpoint = pluginRegistration.endpoint,
                    status = PluginInstanceStatus.STARTING,
                    pluginId = plugin.id,
                )
            )
        }
}
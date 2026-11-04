package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.exception.CustomExceptions
import ru.sage.registryservice.domain.model.PluginCapability
import ru.sage.registryservice.domain.repository.PluginCapabilityRepository

interface GetPluginCapabilitiesUseCase {
    operator fun invoke(pluginId: String): List<PluginCapability>
}

@Service
class GetPluginCapabilitiesUseCaseImpl(
    private val pluginCapabilityRepository: PluginCapabilityRepository
) : GetPluginCapabilitiesUseCase {
    override fun invoke(pluginId: String): List<PluginCapability> = transaction {
        val capabilities = pluginCapabilityRepository.findByPluginId(pluginId)

        if (capabilities.isEmpty())
            throw CustomExceptions.PluginCapabilitiesNotFoundException(
                "Plugin capabilities for '$pluginId' is not found"
            )

        capabilities
    }
}
package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.exception.CustomExceptions
import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.repository.PluginInstanceRepository

interface GetPluginInstancesUseCase {
    operator fun invoke(
        pluginId: String,
        status: PluginInstanceStatus?
    ): List<PluginInstance>
}

@Service
class GetPluginInstancesUseCaseImpl(
    private val pluginInstanceRepository: PluginInstanceRepository
) : GetPluginInstancesUseCase {
    override fun invoke(
        pluginId: String,
        status: PluginInstanceStatus?
    ): List<PluginInstance> = transaction {
        val pluginInstances = pluginInstanceRepository.findByPluginId(pluginId, status)

        if (pluginInstances.isEmpty()) throw CustomExceptions.PluginInstancesNotFoundException("Plugin instances of '$pluginId' with status = '$status' not found")

        pluginInstances
    }
}
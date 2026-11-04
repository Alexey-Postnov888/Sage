package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.exception.CustomExceptions
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.repository.PluginInstanceRepository
import java.util.UUID

interface UnregisterPluginInstanceUseCase {
    operator fun invoke(id: UUID)
}

@Service
class UnregisterPluginInstanceUseCaseImpl(
    private val pluginInstanceRepository: PluginInstanceRepository
) : UnregisterPluginInstanceUseCase {
    override fun invoke(id: UUID) = transaction {
        if (pluginInstanceRepository.updateStatus(id, PluginInstanceStatus.STOPPED) == 0)
            throw CustomExceptions.PluginInstanceNotFoundException(
                "Plugin instance '$id' not found"
            )
    }
}
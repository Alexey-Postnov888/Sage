package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.exception.CustomExceptions
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.repository.PluginInstanceRepository
import java.time.LocalDateTime
import java.util.UUID

interface UpdateHeartbeatUseCase {
    operator fun invoke(id: UUID)
}

@Service
class UpdateHeartbeatUseCaseImpl(
    private val pluginInstanceRepository: PluginInstanceRepository
) : UpdateHeartbeatUseCase {
    override operator fun invoke(id: UUID) {
        val now = LocalDateTime.now()

        return transaction {
            if (pluginInstanceRepository.updateHeartbeat(id, now) == 0)
                throw CustomExceptions.PluginInstanceNotFoundException("Plugin instance '$id' not found")

            pluginInstanceRepository.updateStatus(id, PluginInstanceStatus.ACTIVE)
        }
    }
}
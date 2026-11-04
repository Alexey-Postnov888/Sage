package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.repository.PluginInstanceRepository
import java.time.LocalDateTime

interface UpdateExpiredPluginInstancesUseCase  {
    operator fun invoke(unavailableBefore: LocalDateTime, stoppedBefore: LocalDateTime): Int
}

@Service
class UpdateExpiredPluginInstancesUseCaseImpl(
    private val pluginInstanceRepository: PluginInstanceRepository
) : UpdateExpiredPluginInstancesUseCase {
    override fun invoke(unavailableBefore: LocalDateTime, stoppedBefore: LocalDateTime): Int = transaction {
        pluginInstanceRepository.updateExpiredStatuses(unavailableBefore, stoppedBefore)
    }
}
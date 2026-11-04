package ru.sage.registryservice.infrastructure.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.sage.registryservice.domain.usecase.UpdateExpiredPluginInstancesUseCase
import java.time.LocalDateTime

@Component
class PluginInstanceScheduler(
    private val updateExpiredPluginInstancesUseCase: UpdateExpiredPluginInstancesUseCase
) {
    @Scheduled(fixedDelay = 10000)
    fun markExpiredInstances() {
        val now = LocalDateTime.now()

        updateExpiredPluginInstancesUseCase(
            unavailableBefore = now.minusMinutes(5),
            stoppedBefore = now.minusMinutes(10)
        )
    }
}
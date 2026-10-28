package ru.sage.notificationservice.domain.usecase

import ru.sage.notificationservice.data.utils.ConnectionManager
import ru.sage.notificationservice.domain.model.NotificationEnvelope
import ru.sage.notificationservice.domain.repository.NotificationRepository

interface ProcessNotificationUseCase {
    suspend operator fun invoke(envelope: NotificationEnvelope)
}

class ProcessNotificationUseCaseImpl(
    private val repository: NotificationRepository,
    private val connectionManager: ConnectionManager
) : ProcessNotificationUseCase {
    override suspend fun invoke(envelope: NotificationEnvelope) {
        val savedEnvelope = runCatching {
            repository.saveIfAbsent(envelope)
        }.getOrNull() ?: return

        val isDelivered = connectionManager.send(savedEnvelope.userId, savedEnvelope)

        if (isDelivered) {
            repository.markAsDeleted(savedEnvelope.eventId)
        }
    }
}
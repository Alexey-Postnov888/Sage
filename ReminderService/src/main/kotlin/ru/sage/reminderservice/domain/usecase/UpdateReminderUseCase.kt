package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.time.LocalDateTime
import java.util.UUID

interface UpdateReminderUseCase{
    fun execute(reminderId: UUID, topic: String? = null, remindAt: LocalDateTime? = null): Boolean
}

@Service
class UpdateReminderUseCaseImpl(
    private val repository: ReminderRepository
): UpdateReminderUseCase {
    override fun execute(
        reminderId: UUID,
        topic: String?,
        remindAt: LocalDateTime?
    ): Boolean {
        val oldReminder: Reminder = repository.findByReminderId(reminderId) ?: return false
        val newReminder = Reminder(
            id = oldReminder.id,
            topic = topic ?: oldReminder.topic,
            ownerId = oldReminder.ownerId,
            remindAt = remindAt ?: oldReminder.remindAt,
            createAt = oldReminder.createAt,
        )
        return repository.updateReminderById(reminderId, newReminder)
    }
}
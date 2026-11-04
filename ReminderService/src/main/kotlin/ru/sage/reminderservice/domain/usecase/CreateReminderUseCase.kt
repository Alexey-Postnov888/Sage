package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.time.LocalDateTime
import java.util.UUID

interface CreateReminderUseCase{
    fun execute(topic: String, ownerId: UUID, remindAt: LocalDateTime): Reminder
}

@Service
class CreateReminderUseCaseImpl(
    private val repository: ReminderRepository
) : CreateReminderUseCase {
    override fun execute(
        topic: String,
        ownerId: UUID,
        remindAt: LocalDateTime
    ): Reminder {
        val result : Reminder = Reminder(
            topic = topic,
            ownerId = ownerId,
            remindAt = remindAt
        )
        return repository.save(result)
    }
}
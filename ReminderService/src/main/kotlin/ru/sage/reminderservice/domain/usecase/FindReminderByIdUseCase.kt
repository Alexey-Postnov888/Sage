package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.util.UUID

interface FindReminderByIdUseCase{
    fun execute(reminderId: UUID): Reminder?
}

@Service
class FindReminderByIdUseCaseImpl(
    private val repository: ReminderRepository
): FindReminderByIdUseCase{
    override fun execute(reminderId: UUID): Reminder? {
        return repository.findByReminderId(reminderId)
    }
}
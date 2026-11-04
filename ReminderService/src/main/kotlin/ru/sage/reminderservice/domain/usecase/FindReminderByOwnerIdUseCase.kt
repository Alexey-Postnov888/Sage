package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.util.UUID

interface FindReminderByOwnerIdUseCase{
    fun execute(ownerId: UUID): List<Reminder>?
}

@Service
class FindReminderByOwnerIdUseCaseImpl (
    private val repository: ReminderRepository
) : FindReminderByOwnerIdUseCase{
    override fun execute(ownerId: UUID): List<Reminder>? {
        return repository.findByOwnerId(ownerId)
    }
}
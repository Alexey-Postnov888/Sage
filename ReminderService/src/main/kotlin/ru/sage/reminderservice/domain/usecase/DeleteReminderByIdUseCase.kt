package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.util.UUID

interface DeleteReminderByIdUseCase{
    fun execute(reminderId: UUID): Boolean
}

@Service
class DeleteReminderByIdUseCaseImpl(
    private val repository: ReminderRepository
) : DeleteReminderByIdUseCase {
    override fun execute(reminderId: UUID): Boolean {
        return repository.findByReminderId(reminderId) != null
                && repository.deleteReminderById(reminderId)
    }
}
package ru.sage.reminderservice.domain.usecase

import org.springframework.stereotype.Service
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.time.LocalDateTime

interface ProcessPendingRemindersUseCase{
    fun execute()
}

@Service
class ProcessPendingRemindersUseCaseImpl(
    private val repository: ReminderRepository
): ProcessPendingRemindersUseCase {
    override fun execute(){
        val now = LocalDateTime.now()
        val pending = repository.findPendingReminders(now)

        for (reminder in pending) {
//            TODO отправление напоминалок
            println("🔔 НАПОМИНАНИЕ ДЛЯ [${reminder.ownerId}]: ${reminder.topic}")

            val sentReminder = reminder.copy(isSent = true)
            repository.save(sentReminder)
        }
    }
}
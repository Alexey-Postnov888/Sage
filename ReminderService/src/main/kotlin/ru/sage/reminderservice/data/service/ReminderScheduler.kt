package ru.sage.reminderservice.data.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.sage.reminderservice.domain.usecase.ProcessPendingRemindersUseCase

@Component
class ReminderScheduler(
    private val processPendingRemindersUseCase: ProcessPendingRemindersUseCase
) {
    @Scheduled(fixedRate = 60000) // Запуск каждую минуту
    fun checkReminders() {
        processPendingRemindersUseCase.execute()
    }
}
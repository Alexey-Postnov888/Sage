package ru.sage.reminderservice.domain.repository

import ru.sage.reminderservice.domain.model.Reminder
import java.time.LocalDateTime
import java.util.UUID

interface ReminderRepository {
    fun save(reminder: Reminder): Reminder
    fun findByReminderId(reminderId: UUID): Reminder?
    fun updateReminderById(reminderId: UUID, newReminder: Reminder): Boolean
    fun deleteReminderById(reminderId: UUID): Boolean
    fun findByOwnerId(ownerId: UUID): List<Reminder>?
    fun findPendingReminders(dateTime: LocalDateTime): List<Reminder>
}
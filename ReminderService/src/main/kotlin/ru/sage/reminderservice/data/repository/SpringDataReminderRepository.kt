package ru.sage.reminderservice.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sage.reminderservice.data.model.entity.ReminderEntity
import java.time.LocalDateTime
import java.util.UUID

interface SpringDataReminderRepository : JpaRepository<ReminderEntity, UUID> {
    fun findByRemindAtLessThanEqualAndIsSentFalse(now: LocalDateTime): List<ReminderEntity>
    fun findAllByOwnerId(ownerId: UUID): List<ReminderEntity>
}
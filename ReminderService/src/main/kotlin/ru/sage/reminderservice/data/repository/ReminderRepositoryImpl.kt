package ru.sage.reminderservice.data.repository

import org.springframework.stereotype.Repository
import ru.sage.reminderservice.data.model.entity.ReminderEntity
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.repository.ReminderRepository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class ReminderRepositoryImpl(
    private val springDataRepository: SpringDataReminderRepository
) : ReminderRepository {
    override fun save(reminder: Reminder): Reminder {
        val entity = toEntity(reminder)
        val savedEntity = springDataRepository.save(entity)
        return toDomain(savedEntity)
    }

    override fun findByReminderId(reminderId: UUID): Reminder? {
        return springDataRepository.findById(reminderId)
            .map {toDomain(it) }
            .orElse(null)
    }

    override fun updateReminderById(
        reminderId: UUID,
        newReminder: Reminder
    ): Boolean {
        if (!springDataRepository.existsById(reminderId)) {
            return false
        }
        val entity = toEntity(newReminder)
        entity.id = reminderId
        springDataRepository.save(entity)
        return true
    }

    override fun deleteReminderById(reminderId: UUID): Boolean {
        return if (springDataRepository.existsById(reminderId)) {
            springDataRepository.deleteById(reminderId)
            true
        } else {
            false
        }
    }

    override fun findByOwnerId(ownerId: UUID): List<Reminder> {
        return springDataRepository.findAllByOwnerId(ownerId)
            .map { toDomain(it) }
    }

    override fun findPendingReminders(dateTime: LocalDateTime): List<Reminder> {
        return springDataRepository.findByRemindAtLessThanEqualAndIsSentFalse(dateTime)
            .map { toDomain(it) }
    }

    private fun toDomain(entity: ReminderEntity): Reminder = Reminder(
        id = entity.id,
        topic = entity.topic,
        ownerId = entity.ownerId,
        remindAt = entity.remindAt,
        createAt = entity.createdAt,
        isSent = entity.isSent
    )

    private fun toEntity(domain: Reminder): ReminderEntity = ReminderEntity(
        id = domain.id,
        topic = domain.topic,
        ownerId = domain.ownerId,
        remindAt = domain.remindAt,
        createdAt = domain.createAt,
        isSent = domain.isSent
    )
}
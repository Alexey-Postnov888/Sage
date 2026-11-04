package ru.sage.reminderservice.data.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "reminders")
class ReminderEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column
    var topic: String,

    @Column(name = "owner_id", nullable = false)
    var ownerId: UUID,

    @Column(name = "remind_at", nullable = false)
    var remindAt: LocalDateTime,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var isSent: Boolean = false
)
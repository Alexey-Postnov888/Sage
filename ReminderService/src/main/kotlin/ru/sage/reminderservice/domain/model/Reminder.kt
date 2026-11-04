package ru.sage.reminderservice.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Reminder (
    val id: UUID? = null,
    val topic: String,
    val ownerId: UUID,
    val remindAt: LocalDateTime,
    val createAt: LocalDateTime = LocalDateTime.now(),
    val isSent: Boolean = false
)
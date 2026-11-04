package ru.sage.reminderservice.presentation.model

import java.time.LocalDateTime
import java.util.UUID

data class CreateReminderRequest(
    val topic: String,
    val ownerId: UUID,
    val remindAt: LocalDateTime
)
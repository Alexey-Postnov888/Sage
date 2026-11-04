package ru.sage.reminderservice.presentation.model

import java.time.LocalDateTime

data class UpdateReminderRequest(
    val topic: String? = null,
    val remindAt: LocalDateTime? = null
)
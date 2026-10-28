package ru.sage.notificationservice.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sage.notificationservice.domain.model.NotificationLevel

@Serializable
data class NotificationMetaPresentation(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("level") val level: NotificationLevel
)
package ru.sage.notificationservice.presentation.mapper

import ru.sage.notificationservice.domain.model.NotificationEnvelope
import ru.sage.notificationservice.domain.model.NotificationMeta
import ru.sage.notificationservice.presentation.model.NotificationEnvelopePresentation
import ru.sage.notificationservice.presentation.model.NotificationMetaPresentation

fun NotificationEnvelope.toPresentation(): NotificationEnvelopePresentation = NotificationEnvelopePresentation(
    version = version,
    eventType = eventType,
    authorId = authorId,
    userId = userId,
    entityId = entityId,
    entityType = entityType,
    status = status,
    createdAt = createdAt,
    meta = meta.toPresentation(),
    payload = payload
)

fun NotificationMeta.toPresentation(): NotificationMetaPresentation = NotificationMetaPresentation(
    title = title,
    description = description,
    level = level
)

fun NotificationEnvelopePresentation.toDomain(): NotificationEnvelope = NotificationEnvelope(
    version = version,
    eventType = eventType,
    authorId = authorId,
    userId = userId,
    entityId = entityId,
    entityType = entityType,
    status = status,
    createdAt = createdAt,
    meta = meta.toDomain(),
    payload = payload
)

fun NotificationMetaPresentation.toDomain(): NotificationMeta = NotificationMeta(
    title = title,
    description = description,
    level = level
)
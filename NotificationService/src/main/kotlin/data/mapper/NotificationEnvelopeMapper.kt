package ru.sage.notificationservice.data.mapper

import kotlinx.serialization.json.Json
import ru.sage.notificationservice.data.model.MongoNotificationDocument
import ru.sage.notificationservice.data.model.MongoNotificationMeta
import ru.sage.notificationservice.domain.model.NotificationEnvelope
import ru.sage.notificationservice.domain.model.NotificationMeta

fun NotificationEnvelope.toDto(): MongoNotificationDocument = MongoNotificationDocument(
    eventId = eventId,
    version = version,
    eventType = eventType,
    authorId = authorId,
    userId = userId,
    entityId = entityId,
    entityType = entityType,
    status = status,
    createdAt = createdAt,
    meta = meta.toDto(),
    payload = payload.toString()
)

fun NotificationMeta.toDto(): MongoNotificationMeta = MongoNotificationMeta(
    title = title,
    description = description,
    level = level
)

fun MongoNotificationDocument.toDomain(): NotificationEnvelope = NotificationEnvelope(
    eventId = eventId,
    version = version,
    eventType = eventType,
    authorId = authorId,
    userId = userId,
    entityId = entityId,
    entityType = entityType,
    status = status,
    createdAt = createdAt,
    meta = meta.toDomain(),
    payload = Json.parseToJsonElement(payload)
)

fun MongoNotificationMeta.toDomain(): NotificationMeta = NotificationMeta(
    title = title,
    description = description,
    level = level
)
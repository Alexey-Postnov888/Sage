package ru.sage.notificationservice.data.model.exception

sealed class CustomExceptions(message : String) : Exception(message) {
    class EventExistsException(message: String) : CustomExceptions(message)
}
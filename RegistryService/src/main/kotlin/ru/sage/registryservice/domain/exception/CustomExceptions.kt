package ru.sage.registryservice.domain.exception

sealed class CustomExceptions(message: String) : Exception(message) {
    class PluginNotFoundException(message: String) : CustomExceptions(message)
    class PluginInstancesNotFoundException(message: String) : CustomExceptions(message)
    class PluginInstanceNotFoundException(message: String) : CustomExceptions(message)
    class PluginCapabilitiesNotFoundException(message: String) : CustomExceptions(message)
}
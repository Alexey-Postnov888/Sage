package ru.sage.registryservice.presentation.model

data class PluginResponse(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val manifestVersion: String
)

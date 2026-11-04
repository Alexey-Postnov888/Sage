package ru.sage.registryservice.domain.model

data class Plugin(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val manifestVersion: String,
)

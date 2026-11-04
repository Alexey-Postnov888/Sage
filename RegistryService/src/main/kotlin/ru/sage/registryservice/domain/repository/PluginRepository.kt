package ru.sage.registryservice.domain.repository

import ru.sage.registryservice.domain.model.Plugin

interface PluginRepository {
    fun findById(id: String): Plugin?
    fun save(plugin: Plugin): Plugin
    fun deleteById(id: String)
}
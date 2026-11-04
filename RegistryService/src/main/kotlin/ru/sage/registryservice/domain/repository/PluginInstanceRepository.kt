package ru.sage.registryservice.domain.repository

import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import java.time.LocalDateTime
import java.util.UUID

interface PluginInstanceRepository {
    fun findById(id: UUID): PluginInstance?
    fun save(pluginInstance: PluginInstance): PluginInstance
    fun deleteById(id: UUID)

    fun findByPluginId(
        pluginId: String,
        status: PluginInstanceStatus? = null
    ): List<PluginInstance>
    fun updateExpiredStatuses(unavailableBefore: LocalDateTime, stoppedBefore: LocalDateTime): Int

    fun updateStatus(id: UUID, newStatus: PluginInstanceStatus): Int
    fun updateHeartbeat(id: UUID, newHeartbeatAt: LocalDateTime): Int
}
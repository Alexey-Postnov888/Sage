package ru.sage.registryservice.data.exposed.repository

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository
import ru.sage.registryservice.data.exposed.mapper.toPluginInstance
import ru.sage.registryservice.data.exposed.table.PluginInstancesTable
import ru.sage.registryservice.domain.model.PluginInstance
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.repository.PluginInstanceRepository
import java.time.LocalDateTime
import java.util.*

@Repository
class PluginInstanceRepositoryImpl : PluginInstanceRepository {
    override fun findById(id: UUID): PluginInstance? =
        PluginInstancesTable
            .selectAll()
            .where { PluginInstancesTable.id eq id }
            .singleOrNull()
            ?.toPluginInstance()

    override fun save(pluginInstance: PluginInstance): PluginInstance {
        val now = LocalDateTime.now()

        PluginInstancesTable.upsert(
            PluginInstancesTable.id,
            onUpdate = {
                it[PluginInstancesTable.endpoint] = pluginInstance.endpoint
                it[PluginInstancesTable.status] = pluginInstance.status.name
                it[PluginInstancesTable.pluginId] = pluginInstance.pluginId
                it[PluginInstancesTable.lastHeartbeatAt] = pluginInstance.lastHeartbeatAt
                it[PluginInstancesTable.updatedAt] = now
            }
        ) {
            it[PluginInstancesTable.id] = pluginInstance.id
            it[PluginInstancesTable.endpoint] = pluginInstance.endpoint
            it[PluginInstancesTable.status] = pluginInstance.status.name
            it[PluginInstancesTable.pluginId] = pluginInstance.pluginId
            it[PluginInstancesTable.lastHeartbeatAt] = pluginInstance.lastHeartbeatAt
            it[PluginInstancesTable.createdAt] = now
            it[PluginInstancesTable.updatedAt] = now
        }

        return findById(pluginInstance.id)!!
    }

    override fun deleteById(id: UUID) {
        PluginInstancesTable.deleteWhere { PluginInstancesTable.id eq id }
    }

    override fun findByPluginId(
        pluginId: String,
        status: PluginInstanceStatus?
    ): List<PluginInstance> =
        PluginInstancesTable
            .selectAll()
            .where {
                if (status == null) PluginInstancesTable.pluginId eq pluginId
                else (PluginInstancesTable.pluginId eq pluginId) and (PluginInstancesTable.status eq status.name)
            }
            .map { it.toPluginInstance() }

    override fun updateExpiredStatuses(unavailableBefore: LocalDateTime, stoppedBefore: LocalDateTime): Int {
        val stoppedCount = PluginInstancesTable.update(
            where = {
                (PluginInstancesTable.lastHeartbeatAt less stoppedBefore) and
                        (PluginInstancesTable.status eq PluginInstanceStatus.UNAVAILABLE.name)
            }
        ) {
            it[status] = PluginInstanceStatus.STOPPED.name
            it[updatedAt] = LocalDateTime.now()
        }

        val unavailableCount = PluginInstancesTable.update(
            where = {
                (PluginInstancesTable.lastHeartbeatAt less unavailableBefore) and
                        (PluginInstancesTable.status eq PluginInstanceStatus.ACTIVE.name)
            }
        ) {
            it[status] = PluginInstanceStatus.UNAVAILABLE.name
            it[updatedAt] = LocalDateTime.now()
        }

        return stoppedCount + unavailableCount
    }

    override fun updateStatus(id: UUID, newStatus: PluginInstanceStatus): Int =
        PluginInstancesTable.update(
            where = { PluginInstancesTable.id eq id }
        ) {
            it[status] = newStatus.name
            it[updatedAt] = LocalDateTime.now()
        }

    override fun updateHeartbeat(id: UUID, newHeartbeatAt: LocalDateTime): Int =
        PluginInstancesTable.update(
            where = { PluginInstancesTable.id eq id }
        ) {
            it[lastHeartbeatAt] = newHeartbeatAt
            it[updatedAt] = newHeartbeatAt
        }
}
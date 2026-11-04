package ru.sage.registryservice.presentation.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.sage.registryservice.domain.usecase.UnregisterPluginInstanceUseCase
import ru.sage.registryservice.domain.usecase.UpdateHeartbeatUseCase
import java.util.*

@RestController
@RequestMapping("/api/v1/plugin-instances")
class PluginInstancesController(
    private val updateHeartbeatUseCase: UpdateHeartbeatUseCase,
    private val unregisterPluginInstanceUseCase: UnregisterPluginInstanceUseCase
) {
    @PostMapping("/{instanceId}/heartbeat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateHeartbeat(
        @PathVariable instanceId: UUID
    ) {
        updateHeartbeatUseCase(instanceId)
    }

    @DeleteMapping("/{instanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unregister(
        @PathVariable instanceId: UUID
    ) {
        unregisterPluginInstanceUseCase(instanceId)
    }
}
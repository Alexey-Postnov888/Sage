package ru.sage.registryservice.presentation.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.sage.registryservice.domain.model.PluginInstanceStatus
import ru.sage.registryservice.domain.usecase.GetPluginCapabilitiesUseCase
import ru.sage.registryservice.domain.usecase.GetPluginInstancesUseCase
import ru.sage.registryservice.domain.usecase.GetPluginUseCase
import ru.sage.registryservice.domain.usecase.RegisterPluginUseCase
import ru.sage.registryservice.presentation.mapper.toPluginRegistration
import ru.sage.registryservice.presentation.mapper.toPresentation
import ru.sage.registryservice.presentation.model.ManifestDto
import ru.sage.registryservice.presentation.model.PluginCapabilityResponse
import ru.sage.registryservice.presentation.model.PluginInstanceResponse
import ru.sage.registryservice.presentation.model.PluginResponse

@RestController
@RequestMapping("/api/v1/plugins")
class PluginsController(
    private val registerPluginUseCase: RegisterPluginUseCase,
    private val getPluginUseCase: GetPluginUseCase,
    private val getPluginInstancesUseCase: GetPluginInstancesUseCase,
    private val getPluginCapabilitiesUseCase: GetPluginCapabilitiesUseCase
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerPlugin(
        @RequestBody manifest: ManifestDto,
        request: HttpServletRequest
    ): PluginInstanceResponse {
        val host = request.remoteAddr
        // TODO проверить, может быть port переедет в манифест
        val port = request.remotePort
        val scheme = request.scheme
        val endpoint = "$scheme://$host:$port${manifest.spec.endpoint}"

        val registration = manifest.toPluginRegistration().copy(endpoint = endpoint)

        return registerPluginUseCase(registration).toPresentation()
    }

    @GetMapping("/{pluginId}")
    fun getPlugin(@PathVariable pluginId: String): PluginResponse =
        getPluginUseCase(pluginId).toPresentation()

    @GetMapping("/{pluginId}/instances")
    fun getPluginInstances(
        @PathVariable pluginId: String,
        @RequestParam(required = false) status: PluginInstanceStatus?
    ): List<PluginInstanceResponse> = getPluginInstancesUseCase(
            pluginId,
            status = status
        ).map {
            it.toPresentation()
        }

    @GetMapping("/{pluginId}/capabilities")
    fun getPluginCapabilities(
        @PathVariable pluginId: String,
    ): List<PluginCapabilityResponse> = getPluginCapabilitiesUseCase(pluginId)
            .map { it.toPresentation() }
}
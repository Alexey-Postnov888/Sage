package ru.sage.registryservice.domain.usecase

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import ru.sage.registryservice.domain.exception.CustomExceptions
import ru.sage.registryservice.domain.model.Plugin
import ru.sage.registryservice.domain.repository.PluginRepository

interface GetPluginUseCase {
    operator fun invoke(id: String): Plugin
}

@Service
class GetPluginUseCaseImpl(
    private val pluginRepository: PluginRepository
) : GetPluginUseCase {
    override fun invoke(id: String): Plugin = transaction {
        pluginRepository.findById(id) ?: throw CustomExceptions.PluginNotFoundException("Plugin '$id' not found")
    }
}
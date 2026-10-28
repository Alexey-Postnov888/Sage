package ru.sage.notificationservice.data.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.rabbitmq.client.ConnectionFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.sage.notificationservice.data.config.JwtConfig
import ru.sage.notificationservice.data.config.MongoConfig
import ru.sage.notificationservice.data.config.RabbitConfig
import ru.sage.notificationservice.data.repository.NotificationRepositoryImpl
import ru.sage.notificationservice.data.utils.ConnectionManager
import ru.sage.notificationservice.data.utils.ConnectionManagerImpl
import ru.sage.notificationservice.domain.repository.NotificationRepository
import ru.sage.notificationservice.domain.usecase.ProcessNotificationUseCase
import ru.sage.notificationservice.domain.usecase.ProcessNotificationUseCaseImpl
import ru.sage.notificationservice.presentation.consumer.RabbitMqConsumer
import ru.sage.notificationservice.presentation.route.grpc.NotificationGrpcService
import ru.sage.notificationservice.presentation.route.grpc.interceptor.JwtAuthInterceptor

val configModule = module {
    single { JwtConfig.fromEnv() }
    single { MongoConfig.fromEnv() }
    single { RabbitConfig.fromEnv() }
}
val networkModule = module {
    single {
        val config = get<MongoConfig>()
        MongoClient.create(config.connectionUri)
    }

    single {
        val config = get<MongoConfig>()

        val mongoDatabase = config.database
        get<MongoClient>().getDatabase(mongoDatabase)
    }

    single {
        val config = get<RabbitConfig>()

        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.username
            password = config.password
            isAutomaticRecoveryEnabled = true
        }
    }

    singleOf(::ConnectionManagerImpl) bind ConnectionManager::class
}

val repositoryModule = module {
    singleOf(::NotificationRepositoryImpl) bind NotificationRepository::class
}

val useCaseModule = module {
    singleOf(::ProcessNotificationUseCaseImpl) bind ProcessNotificationUseCase::class
}

val consumerModule = module {
    singleOf(::RabbitMqConsumer)
}

val grpcModule = module {
    singleOf(::JwtAuthInterceptor)
    singleOf(::NotificationGrpcService)
}

val appModule = module {
    includes(
        networkModule,
        configModule,
        repositoryModule,
        useCaseModule,
        consumerModule,
        grpcModule
    )
}
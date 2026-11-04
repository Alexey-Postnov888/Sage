package ru.sage.registryservice.data.exposed

import org.jetbrains.exposed.v1.jdbc.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseFactory {
    @Bean
    fun database(dataSource: DataSource): Database = Database.connect(dataSource)
}
package ru.sage.reminderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ReminderServiceApplication

fun main(args: Array<String>) {
    runApplication<ReminderServiceApplication>(*args)
}

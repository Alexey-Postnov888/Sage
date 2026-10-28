package ru.sage.reminderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReminderServiceApplication

fun main(args: Array<String>) {
    runApplication<ReminderServiceApplication>(*args)
}

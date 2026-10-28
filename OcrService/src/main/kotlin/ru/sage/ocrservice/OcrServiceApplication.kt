package ru.sage.ocrservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OcrServiceApplication

fun main(args: Array<String>) {
    runApplication<OcrServiceApplication>(*args)
}

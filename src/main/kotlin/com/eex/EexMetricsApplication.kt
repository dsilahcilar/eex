package com.eex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EexMetricsApplication

fun main(args: Array<String>) {
    runApplication<EexMetricsApplication>(*args)
} 
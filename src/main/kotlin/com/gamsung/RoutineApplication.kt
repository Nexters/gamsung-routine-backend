package com.gamsung

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RoutineApplication

fun main(args: Array<String>) {
    runApplication<RoutineApplication>(*args)
    println("\n\n* * * Application start!!! * * * \n")
}

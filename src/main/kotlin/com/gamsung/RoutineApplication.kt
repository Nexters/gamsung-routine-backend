package com.gamsung

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@EnableScheduling
@SpringBootApplication
class RoutineApplication {
    @PostConstruct
    fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")))
    }
}

fun main(args: Array<String>) {
    runApplication<RoutineApplication>(*args)
    println("\n\n* * * Application start!!! * * * \n")
}

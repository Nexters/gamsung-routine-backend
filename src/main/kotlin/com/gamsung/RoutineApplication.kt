package com.gamsung

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct

@EnableScheduling
@SpringBootApplication
@RestController
class RoutineApplication {
    @PostConstruct
    fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")))
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.HEAD, RequestMethod.GET])
    fun index(): String {
        return "Hello BonkaeMaster"
    }
}

fun main(args: Array<String>) {
    runApplication<RoutineApplication>(*args)
    println("\n\n* * * Application start!!! * * * \n")
}

package com.gamsung.api.unit

import com.fasterxml.jackson.databind.ObjectMapper
import com.gamsung.configuration.SecurityConfig
import com.gamsung.domain.routine.RoutineTaskService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class RoutineTaskUnitScheduler (
    val routineTaskService: RoutineTaskService
) {

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedDelay = 5000)
    fun generateUnit() {
        routineTaskService.getAndSaveTodayRoutineUnits()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
        private val OBJECT_MAPPER = ObjectMapper()
    }
}

package com.gamsung.repository

import com.gamsung.domain.routine.RoutineTaskUnit
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface RoutineTaskUnitRepository: MongoRepository<RoutineTaskUnit, String> {
//    fun findByProfileIdAndCompletedAtBetween(profileId: String, start: LocalDateTime, end: LocalDateTime): List<RoutineTaskUnit>
}

package com.gamsung.repository

import com.gamsung.domain.routine.RoutineTaskHistory
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface RoutineTaskHistoryRepository: MongoRepository<RoutineTaskHistory, String> {
    fun findByProfileIdAndCompletedAtBetween(profileId: String, start: LocalDateTime, end: LocalDateTime): List<RoutineTaskHistory>
}

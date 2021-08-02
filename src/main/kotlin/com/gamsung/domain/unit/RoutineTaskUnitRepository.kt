package com.gamsung.domain.unit

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface RoutineTaskUnitRepository : MongoRepository<RoutineTaskUnit, String> {
    fun findByProfileIdAndLocalDateBetween(
        profileId: String,
        start: LocalDate,
        end: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findByProfileId(profileId: String): MutableList<RoutineTaskUnit>
    fun findByTaskIdIn(taskIds: List<String>): MutableList<RoutineTaskUnit>
}

package com.gamsung.domain.unit

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface RoutineTaskUnitRepository : MongoRepository<RoutineTaskUnit, String> {
    fun findByProfileIdAndDateBetween(
        profileId: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): MutableList<RoutineTaskUnit>

    fun findByProfileId(profileId: String): MutableList<RoutineTaskUnit>
}
package com.gamsung.domain.unit

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RoutineTaskUnitRepository : MongoRepository<RoutineTaskUnit, String> {
    fun findByUnitIdAndDelayedDateTimeIsNull(unitId: String): List<RoutineTaskUnit>

    fun findAllByProfileIdAndLocalDateAndDelayedDateTimeIsNull(profileId: String, date: LocalDate): List<RoutineTaskUnit>

    fun findAllByTaskIdAndLocalDateAndDelayedDateTimeIsNull(taskId: String, date: LocalDate): List<RoutineTaskUnit>

    fun findAllByProfileIdAndLocalDateBetweenAndDelayedDateTimeIsNull(
        profileId: String,
        start: LocalDate,
        end: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findAllByProfileIdAndTaskIdAndLocalDateAndDelayedDateTimeIsNull(
        profileId: String,
        taskId: String,
        localDate: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findByProfileId(profileId: String): MutableList<RoutineTaskUnit>

    fun findByTaskIdInAndDelayedDateTimeIsNull(taskIds: List<String>): MutableList<RoutineTaskUnit>
}

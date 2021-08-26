package com.gamsung.domain.unit

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface RoutineTaskUnitRepository : MongoRepository<RoutineTaskUnit, String> {
    fun findByUnitId(unitId: String): Optional<RoutineTaskUnit>
    fun findByUnitIdAndDelayedDateTimeIsNull(unitId: String): List<RoutineTaskUnit>

    fun findAllByProfileIdAndLocalDateAndDelayedDateTimeIsNull(
        profileId: String,
        date: LocalDate
    ): List<RoutineTaskUnit>

    fun findAllByTaskIdAndLocalDateAndDelayedDateTimeIsNull(taskId: String, date: LocalDate): List<RoutineTaskUnit>

    fun findAllByProfileIdAndLocalDateBetweenAndDelayedDateTimeIsNull(
        profileId: String,
        start: LocalDate,
        end: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findAllByProfileIdAndTaskIdAndLocalDateBetweenAndDelayedDateTimeIsNull(
        profileId: String,
        taskId: String,
        start: LocalDate,
        end: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findAllByProfileIdAndTaskIdAndDateBetweenAndDelayedDateTimeIsNull(
        profileId: String,
        taskId: String,
        start: String,
        end: String
    ): MutableList<RoutineTaskUnit>

    fun findAllByProfileIdAndTaskIdAndLocalDateAndDelayedDateTimeIsNull(
        profileId: String,
        taskId: String,
        localDate: LocalDate
    ): MutableList<RoutineTaskUnit>

    fun findAllByProfileIdAndTaskIdAndDateAndDelayedDateTimeIsNull(
        profileId: String,
        taskId: String,
        date: String
    ): List<RoutineTaskUnit>

    fun findByProfileId(profileId: String): MutableList<RoutineTaskUnit>

    fun findAllByDateAndUnitIdIn(date: String, unitIds: List<String>): List<RoutineTaskUnit>

    fun findByTaskCodeInAndDelayedDateTimeIsNull(taskIds: List<String>): MutableList<RoutineTaskUnit>

    fun findByTaskCodeIn(codes: List<String>): MutableList<RoutineTaskUnit>

    fun deleteByUnitId(unitId: String)

}

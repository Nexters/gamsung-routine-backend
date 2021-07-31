package com.gamsung.domain.routine

import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {

    fun createRoutineTaskUnit(routineTaskUnit: RoutineTaskUnit): RoutineTaskUnit {
        val unit = RoutineTaskUnit(
            id = routineTaskUnit.id,
            profileId = routineTaskUnit.profileId,
            date = routineTaskUnit.date,
            localDate = routineTaskUnit.localDate,
            taskId = routineTaskUnit.taskId,
            title = routineTaskUnit.title,

            timesOfWeek = routineTaskUnit.timesOfWeek,
            timesOfDay = routineTaskUnit.timesOfDay,
            days = routineTaskUnit.days,
            times = routineTaskUnit.times,

            friendIds = null,
            completeCount = 0, // 시작은 0
            completedDateList = mutableListOf()
        )

        return routineTaskUnitRepository.save(unit)
    }

    fun getRoutineTaskUnitAll(profileId: String): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileId(profileId)
    }

    fun getRoutineTaskUnit(
        profileId: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileIdAndDateBetween(
            profileId, fromDate, toDate
        )
    }

}

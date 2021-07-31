package com.gamsung.domain.unit

import com.gamsung.api.dto.RoutineTaskUnitDto
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {

    fun createRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        return RoutineTaskUnit(
            id = null,
            profileId = routineTaskUnitDto.profileId,
            date = routineTaskUnitDto.date,
            localDate = LocalDate.now(),
            taskId = routineTaskUnitDto.taskId,
            title = routineTaskUnitDto.title,

            timesOfDay = routineTaskUnitDto.timesOfDay,
            days = routineTaskUnitDto.days,
            times = routineTaskUnitDto.times,

            friendIds = null,
            completeCount = 0, // 시작은 0
            completedDateList = mutableListOf()
        )
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

package com.gamsung.domain.unit

import com.gamsung.api.dto.RoutineTaskUnitDto
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {

    fun createRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val date = generateDate(LocalDate.now())
        val id = date.plus(":").plus(routineTaskUnitDto.profileId).plus(":").plus(routineTaskUnitDto.taskId)
        val unit = RoutineTaskUnit.create(
            id = id,
            profileId = routineTaskUnitDto.profileId,
            date = date,
            localDate = LocalDate.now(),
            taskId = routineTaskUnitDto.taskId,
            title = routineTaskUnitDto.title,

            timesOfDay = routineTaskUnitDto.timesOfDay,
            days = routineTaskUnitDto.days,
            times = routineTaskUnitDto.times,

            friendIds = null,
            completeCount = 0, // 시작은 0
        )

        return routineTaskUnitRepository.save(unit)
    }

    fun getRoutineTaskUnitAll(profileId: String): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileId(profileId)
    }

    fun getRoutineTaskUnit(
        profileId: String,
        fromDate: LocalDate,
        toDate: LocalDate
    ): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileIdAndLocalDateBetween(
            profileId, fromDate, toDate
        )
    }

    private fun generateDate(currDate: LocalDate) : String {
        val monthString = currDate.month.value.toString()
        val month = if (monthString.length < 2) ("0$monthString") else monthString

        val dayString = currDate.dayOfMonth.toString()
        val day = if (dayString.length < 2) ("0$dayString") else dayString

        return currDate.year.toString() + month + day
    }

}

package com.gamsung.domain.routine

import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {

    fun createRoutineTaskUnit(routineTask: RoutineTask, date: LocalDate): RoutineTaskUnit {
        val unit = RoutineTaskUnit(
            id = date.toString().plus(":").plus(routineTask.profileId).plus(":").plus(routineTask.id),
            profileId = routineTask.profileId,
            date = date.toString(), // todo
            taskId = routineTask.id, // todo : String???
            title = routineTask.title,

            timesOfWeek = routineTask.timesOfWeek,
            timesOfDay = routineTask.timesOfDay,
            days = routineTask.days,
            times = routineTask.times,

            friendIds = null,
            completeCount = 0, // 시작은 0
            completedDateList = mutableListOf()
        )

        return routineTaskUnitRepository.save(unit)
    }
}

package com.gamsung.domain.routine

import com.gamsung.api.dto.MonthlyRoutineHistoryDto
import com.gamsung.api.dto.RoutineTaskDto
import com.gamsung.domain.unit.RoutineTaskUnit
import com.gamsung.domain.unit.RoutineTaskUnitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@Service
class RoutineTaskService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskRepository: RoutineTaskRepository
) {
//    fun getUserRoutines(profileId: String): RoutineDto {
//        val routineTasks = routineTaskRepository.findByProfileId(profileId)
//
//        val routinesDaily = mutableMapOf<String, List<RoutineTask>>()
//
//        for (routineTask in routineTasks) {
//            routineTask.days?.let {
//                for (day in it) {
//                    var routines = routinesDaily.getOrDefault(day, mutableListOf())
//                    routines
//                }
//            }
//        }
//
//        return RoutineDto (routine = routineTasks.map { it.toDto() }.groupBy { it.days })
//    }

    fun getMonthlyRoutines(profileId: String, year: Int?, month: Int?): MonthlyRoutineHistoryDto {
        if (year != null && month != null) {
            val lastMonth = Month.of(month).minus(1)
            val year = if (lastMonth.value == 1) (year - 1) else year
            val start = LocalDateTime.of(year, lastMonth, 1, 0, 0)
            val isLeapYear = LocalDate.ofYearDay(year, 1).isLeapYear
            val end = LocalDateTime.of(year, Month.of(month), Month.of(month).length(isLeapYear), 23, 59)
            val dailyRoutines = routineTaskUnitRepository.findByProfileIdAndDateBetween(profileId, start, end)
            val routineTasks = routineTaskRepository.findByProfileId(profileId)
            val today = LocalDate.now()
            for (routineTask in routineTasks) {
                routineTask.days.let {
                    for (day in it) {
                        if (day <= today.dayOfWeek.value) {
                            continue
                        }

                        val daysFromToday = day - today.dayOfWeek.value
                        val currDate = LocalDate.now().plusDays(daysFromToday.toLong())

                        val monthString = currDate.month.value.toString()
                        val month = if (monthString.length < 2) ("0$monthString") else monthString

                        val dayString = currDate.dayOfMonth.toString()
                        val day = if (dayString.length < 2) ("0$dayString") else dayString

                        val date = currDate.year.toString() + month + day
                        val id = date.plus(":").plus(routineTask.profileId).plus(":").plus(routineTask.id)

                        val dailyTaskUnit = RoutineTaskUnit(
                            id = id,
                            profileId = routineTask.profileId,
                            date = date,
                            localDate = LocalDate.now(),
                            taskId = routineTask.id,
                            title = routineTask.title,
                            timesOfDay = routineTask.timesOfDay,
                            days = routineTask.days,
                            times = routineTask.times,
                            completeCount = 0,
                            completedDateList = mutableListOf(),
                            friendIds = arrayListOf()
                        )

                        dailyRoutines.add(dailyTaskUnit)
                    }
                }
            }
            return MonthlyRoutineHistoryDto(
                year = year,
                month = month,
                dailyRoutines = dailyRoutines.groupBy { it.date })
        } else {
            throw Exception()
        }
    }

    /**
     * 1. create task
     * 2. create task unit
     */
    fun createRoutineTask(routineTaskDto: RoutineTaskDto) {
        //
    }

}

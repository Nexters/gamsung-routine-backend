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

    fun getMonthlyRoutines(profileId: String, year: Int, month: Int): MonthlyRoutineHistoryDto {
        val lastMonth = Month.of(month).minus(1).value
        val startMonth = if (lastMonth == 0) 12 else lastMonth
        val startYear = if (startMonth == 12) (year - 1) else year
        val startDate = LocalDate.of(startYear, startMonth, 1)

        val nextMonth = Month.of(month).plus(1).value
        val endMonth = if (nextMonth == 13) 1 else nextMonth
        val endYear = if (endMonth == 1) (year + 1) else year
        val isLeapYear = LocalDate.ofYearDay(endYear, 1).isLeapYear
        val endDate = LocalDate.of(endYear, Month.of(endMonth), Month.of(endMonth).length(isLeapYear))

        val dailyRoutines = routineTaskUnitRepository.findByProfileIdAndLocalDateBetween(profileId, startDate, endDate)
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
                    val date = generateDate(currDate)
                    val id = date.plus(":").plus(profileId).plus(":").plus(routineTask.id)
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

        dailyRoutines.sortBy { it.date.toInt() }

        return MonthlyRoutineHistoryDto(
            year = year,
            month = month,
            dailyRoutines = dailyRoutines.groupBy { it.date })

    }

    fun getAndSaveTodayRoutineUnits() {
        val routineTaskUnits = mutableListOf<RoutineTaskUnit>()
        val routineTasks = routineTaskRepository.findAll()
        val today = LocalDate.now()
        for (routineTask in routineTasks) {
            routineTask.days.let {
                val daySet = it.toSet()
                if (daySet.contains(today.dayOfWeek.value)) {
                    val date = generateDate(LocalDate.now())
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
                    routineTaskUnits.add(dailyTaskUnit)
                }
            }
        }
        routineTaskUnitRepository.saveAll(routineTaskUnits)
    }

    private fun generateDate(currDate: LocalDate) : String {
        val monthString = currDate.month.value.toString()
        val month = if (monthString.length < 2) ("0$monthString") else monthString

        val dayString = currDate.dayOfMonth.toString()
        val day = if (dayString.length < 2) ("0$dayString") else dayString

        return currDate.year.toString() + month + day
    }

    /**
     * 1. create task
     * 2. create task unit
     */
    fun createRoutineTask(routineTaskDto: RoutineTaskDto) {
        //
    }

}

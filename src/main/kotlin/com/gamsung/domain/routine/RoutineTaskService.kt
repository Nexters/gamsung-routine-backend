package com.gamsung.domain.routine

import com.gamsung.repository.RoutineTaskRepository
import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.stereotype.Service

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

//    fun getMonthlyRoutines(profileId: String, year: Int?, month: Int?): MonthlyRoutineHistoryDto {
//        if (year != null && month != null) {
//            val start = LocalDateTime.of(year, Month.of(month), 1, 0, 0)
//            val isLeapYear = LocalDate.ofYearDay(year, 1).isLeapYear
//            val end = LocalDateTime.of(year, Month.of(month), Month.of(month).length(isLeapYear), 23, 59)
//            val routineTaskHistories =
//                routineTaskUnitRepository.findByProfileIdAndCompletedAtBetween(profileId, start, end)
//            return MonthlyRoutineHistoryDto(year = year, month = month, dailyRoutineHistory = routineTaskHistories.groupBy { it.completedAt.dayOfMonth })
//        } else {
//            throw Exception()
//        }
//    }
}

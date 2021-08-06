package com.gamsung.domain.routine

import com.gamsung.api.dto.*
import com.gamsung.domain.unit.RoutineTaskUnit
import com.gamsung.domain.unit.RoutineTaskUnitRepository
import com.gamsung.generateDate
import org.springframework.stereotype.Service
import java.time.LocalDate
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

        val routineUnits = routineTaskUnitRepository.findAllByProfileIdAndLocalDateBetween(profileId, startDate, endDate)

        //초대가 되면 같은 task id를 가지게 됨 (다른 profile id)
        //내가 가지고 있는 모든 taskId를 바탕으로 친구들의 taskUnit까지 가져옴, 그리고 20210712:taskId 등으로 해서 같은 태스크 끼리 모음
        val friendRoutineUnits = routineTaskUnitRepository.findByTaskIdIn(routineUnits.map { it.taskId })
        //key 값에 따라 친구 task grouping 을 함
        val friendRoutinesUnitsByDateAndTaskId = friendRoutineUnits.groupBy { it.date + it.taskId }

        val routineUnitDtoResult = mutableListOf<RoutineTaskUnitDto>()
        routineUnits.forEach { dailyRoutine ->
            val key = dailyRoutine.date + dailyRoutine.taskId
            //만약 같은 key가 존재 하면 친구 task를 dto friends 필드에 입력
            if (friendRoutinesUnitsByDateAndTaskId.containsKey(key)) {
                val friendRoutines = friendRoutinesUnitsByDateAndTaskId[key]!!
                    .map {
                        RoutineTaskFriendUnitDto(
                            profileId = it.profileId,
                            completeCount = it.completeCount,
                            completedDateList = it.completedDateList
                        )
                    }
                val newUnitDto = dailyRoutine.toDto(friendRoutines)
                routineUnitDtoResult.add(newUnitDto)
            }
        }

        // 여기서 부터는 미래에 있는 task들 오늘 이후
        val today = LocalDate.now()
        if (startDate.isBefore(today) && endDate.isAfter(today)) {
            val routineTasks = routineTaskRepository.findByProfileId(profileId)

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
                            localDate = currDate,
                            taskId = routineTask.id.toString(),
                            title = routineTask.title,
                            days = routineTask.days,
                            times = routineTask.times,
                            completeCount = 0,
                            completedDateList = mutableListOf(),
                            friendIds = arrayListOf()
                        )

                        routineUnitDtoResult.add(dailyTaskUnit.toDto(arrayListOf()))
                    }
                }
            }
        }

        routineUnitDtoResult.sortBy { it.date.toInt() }

        return MonthlyRoutineHistoryDto(
            year = year,
            month = month,
            dailyRoutines = routineUnitDtoResult.groupBy { it.date })

    }

    fun getAndSaveTodayRoutineUnits() {
        val routineTaskUnits = mutableListOf<RoutineTaskUnit>()
        val routineTasks = routineTaskRepository.findAll()
        val today = LocalDate.now()
        loop@ for (routineTask in routineTasks) {

            // 미루기를 통해 이미 해당 일정에 태스크 유닛이 있는지 확인
            val unit = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndLocalDate(
                routineTask.profileId,
                routineTask.id.toString(),
                today
            )
            if (unit.size > 0) continue@loop

            routineTask.days.let {
                val daySet = it.toSet()
                if (daySet.contains(today.dayOfWeek.value)) {
                    val date = generateDate(today)
                    val id = date.plus(":").plus(routineTask.profileId).plus(":").plus(routineTask.id)
                    val dailyTaskUnit = RoutineTaskUnit(
                        id = id,
                        profileId = routineTask.profileId,
                        date = date,
                        localDate = today,
                        taskId = routineTask.id.toString(),
                        title = routineTask.title,
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

//    private fun generateDate(currDate: LocalDate) : String {
//        val monthString = currDate.month.value.toString()
//        val month = if (monthString.length < 2) ("0$monthString") else monthString
//
//        val dayString = currDate.dayOfMonth.toString()
//        val day = if (dayString.length < 2) ("0$dayString") else dayString
//
//        return currDate.year.toString() + month + day
//    }

    fun inviteFriendToTask(taskId: String, friendId: String): RoutineTask {

        val task = routineTaskRepository.findById(taskId)
        if (task.isPresent) {
            val friendTask = RoutineTask(
                id = task.get().id,
                profileId = friendId,
                title = task.get().title,
//                timesOfWeek = task.get().timesOfWeek,
//                timesOfDay = task.get().timesOfDay,
                notify = task.get().notify,
                days = task.get().days,
                times = task.get().times,
                category = task.get().category,
                templateId = task.get().templateId,
                order = task.get().order
            )

            return routineTaskRepository.save(friendTask)
        } else {
            throw Exception("Task not exist")
        }
    }

}

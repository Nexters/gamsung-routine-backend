package com.gamsung.domain.routine

import com.gamsung.api.dto.*
import com.gamsung.domain.auth.repository.UserRepository
import com.gamsung.domain.unit.RoutineTaskUnit
import com.gamsung.domain.unit.RoutineTaskUnitRepository
import com.gamsung.infra.toDateString
import feign.FeignException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Month

@Service
class RoutineTaskService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskRepository: RoutineTaskRepository,
    private val userRepository: UserRepository
) {

    fun save(routineTaskDto: RoutineTaskDto): RoutineTask {
        val routineTaskSaved = routineTaskRepository.save(routineTaskDto.toNewEntity())
        val routineTaskUnits = mutableListOf<RoutineTaskUnit>()
        getTodayRoutineTaskUnit(routineTaskUnits, routineTaskSaved)
        routineTaskUnitRepository.saveAll(routineTaskUnits)
        return routineTaskSaved
    }

    fun update(routineTaskDto: RoutineTaskDto): RoutineTask {
        val savedRoutineTask = routineTaskRepository.findById(routineTaskDto.id ?: "")
        if (savedRoutineTask.isEmpty) throw IllegalArgumentException("업데이트 할 Task를 찾을 수 없습니다.")

        val dayOfWeek = LocalDate.now().dayOfWeek.value
        val unitId = savedRoutineTask.get().getUnitId(LocalDate.now().toDateString())

        if (routineTaskDto.days.contains(dayOfWeek)) {
            //update unit
            val todayUnit = routineTaskUnitRepository.findByUnitId(unitId)
            if (todayUnit.isPresent) {
                todayUnit.get().title = routineTaskDto.title
                todayUnit.get().times = routineTaskDto.times
                routineTaskUnitRepository.save(todayUnit.get());
            }
        } else {
            //delete unit
            routineTaskUnitRepository.deleteByUnitId(unitId)
        }
        return routineTaskRepository.save(routineTaskDto.toEntity())
    }

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

        val routineUnits = routineTaskUnitRepository.findAllByProfileIdAndLocalDateBetweenAndDelayedDateTimeIsNull(profileId, startDate, endDate)

        //초대가 되면 같은 code를 가지게 됨 (다른 profile id)
        //내가 가지고 있는 모든 taskId를 바탕으로 친구들의 taskUnit까지 가져옴, 그리고 20210712:taskId 등으로 해서 같은 태스크 끼리 모음
        val friendRoutineUnits = routineTaskUnitRepository.findByTaskCodeIn(routineUnits.map { it.taskCode })
        //key 값에 따라 친구 task grouping 을 함
        val friendRoutinesUnitsByDateAndTaskId = friendRoutineUnits.groupBy { it.date + it.taskCode }

        val routineUnitDtoResult = mutableListOf<RoutineTaskUnitDto>()


        val usersMap = userRepository.findByIdIn(friendRoutineUnits.map { it.profileId }.distinct()).associateBy { it.id }

        routineUnits.forEach { dailyRoutine ->
            val key = dailyRoutine.date + dailyRoutine.taskCode
            //만약 같은 key가 존재 하면 친구 task를 dto friends 필드에 입력
            if (friendRoutinesUnitsByDateAndTaskId.containsKey(key)) {
                val friendRoutines = friendRoutinesUnitsByDateAndTaskId[key]!!
                    .map {
                        usersMap[it.profileId]?.let { user ->
                            RoutineTaskFriendUnitDto(
                                taskId = it.taskId,
                                profileId = it.profileId,
                                name = user.nickname,
                                profileImageUrl = user.profileImageUrl.orEmpty(),
                                thumbnailImageUrl = user.thumbnailImageUrl.orEmpty(),
                                completeCount = it.completeCount,
                                completedDateList = it.completedDateList
                            )
                        }
                    }
                val newUnitDto = dailyRoutine.toDto(friendRoutines as List<RoutineTaskFriendUnitDto>)
                routineUnitDtoResult.add(newUnitDto)
            } else {
                routineUnitDtoResult.add(dailyRoutine.toDto())
            }
        }

        // 여기서 부터는 미래에 있는 task들 오늘 이후
        val today = LocalDate.now()
        if (startDate.isBefore(today) && endDate.isAfter(today)) {
            val routineTasks = routineTaskRepository.findByProfileId(profileId)
            val routineTasksByCode = routineTaskRepository.findByCodeIn(routineTasks.map { it.code!! })
            val routineTaskGroupByCode = routineTasksByCode.groupBy { it.code }
            val usersMap = userRepository.findByIdIn(routineTasksByCode.map { it.profileId }.distinct()).associateBy { it.id }

            for (routineTask in routineTasks) {
                routineTask.days.let {
                    for (day in it) {
                        if (day <= today.dayOfWeek.value) {
                            continue
                        }

                        val daysFromToday = day - today.dayOfWeek.value
                        val currDate = LocalDate.now().plusDays(daysFromToday.toLong())
                        val date = currDate.toDateString()
                        val unitId = date.plus(":").plus(profileId).plus(":").plus(routineTask.id)
                        val dailyTaskUnit = RoutineTaskUnit(
                            unitId = unitId,
                            profileId = routineTask.profileId,
                            date = date,
                            localDate = currDate,
                            taskId = routineTask.id.toString(),
                            taskCode = routineTask.code!!,
                            title = routineTask.title,
                            days = routineTask.days,
                            times = routineTask.times,
                            completedDateList = mutableListOf(),
                            friendIds = arrayListOf(),
                            delayedDateTime = null
                        )

                        val friends = routineTaskGroupByCode[routineTask.code!!]!!
                            .map {
                                usersMap[it.profileId]?.let { user ->
                                    RoutineTaskFriendUnitDto(
                                        taskId = it.id!!,
                                        profileId = it.profileId,
                                        name = user.nickname,
                                        profileImageUrl = user.profileImageUrl.orEmpty(),
                                        thumbnailImageUrl = user.thumbnailImageUrl.orEmpty(),
                                        completeCount = 0,
                                        completedDateList = emptyList()
                                    )
                                }
                            }
                        routineUnitDtoResult.add(dailyTaskUnit.toDto(friends as List<RoutineTaskFriendUnitDto>))
                    }
                }
            }
        }

        routineUnitDtoResult.sortBy { it.date?.toInt() }

        return MonthlyRoutineHistoryDto(
            year = year,
            month = month,
            dailyRoutines = routineUnitDtoResult.groupBy { it.date ?: "" })

    }


    fun getAndSaveTodayRoutineUnits() {
        val routineTaskUnits = mutableListOf<RoutineTaskUnit>()
        val routineTasks = routineTaskRepository.findAll()
        loop@ for (routineTask in routineTasks) {
            getTodayRoutineTaskUnit(routineTaskUnits, routineTask)
        }

        val routineTaskIds = routineTaskUnits.map { it.unitId }
        val existingRoutineUnitSet = routineTaskUnitRepository.findAllByDateAndUnitIdIn(LocalDate.now().toDateString(), routineTaskIds)
            .map { it.unitId }.toSet()
        val filteredTaskUnits = routineTaskUnits.filter { !existingRoutineUnitSet.contains(it.unitId) }
        routineTaskUnitRepository.saveAll(filteredTaskUnits)
    }

    fun getTodayRoutineTaskUnit(routineTaskUnits: MutableList<RoutineTaskUnit>, routineTask: RoutineTask) {
        val today = LocalDate.now()

        routineTask.days.let {
            val daySet = it.toSet()
            if (daySet.contains(today.dayOfWeek.value) || routineTask.delayCount > 0) {

                // 수행 날짜도 아닌데 진입 했다면 delay된 유닛
                val delayStatus = !daySet.contains(today.dayOfWeek.value)
                if (delayStatus) {
                    val dto = routineTask.toDto()
                    dto.delayCount--
                    routineTaskRepository.save(dto.toEntity())
                }

                val date = today.toDateString()
                val unitId = date.plus(":").plus(routineTask.profileId).plus(":").plus(routineTask.id)
                val dailyTaskUnit = RoutineTaskUnit(
                    unitId = unitId,
                    profileId = routineTask.profileId,
                    date = date,
                    localDate = today,
                    taskId = routineTask.id!!,
                    taskCode = routineTask.code!!,
                    title = routineTask.title,
                    days = routineTask.days,
                    times = routineTask.times,
                    completedDateList = mutableListOf(),
                    friendIds = arrayListOf(),
                    isDelayUnit = delayStatus,
                    delayedDateTime = null
                )
                routineTaskUnits.add(dailyTaskUnit)
            }
        }

    }

    // 단일 task 조회를 위한 함수
    fun getRoutineTask(id: String): RoutineTaskDto {
        val routineTask = routineTaskRepository.findById(id)
        if (routineTask.isPresent) {
            val friendRoutineTasks = routineTaskRepository.findByCode(routineTask.get().code!!)
            val usersMap = userRepository.findByIdIn(friendRoutineTasks.map { it.profileId }).associateBy { it.id }
            val friends = friendRoutineTasks.map {
                usersMap[it.profileId]?.let { user ->
                    RoutineTaskFriendDto(
                        taskId = it.id!!,
                        profileId = it.profileId,
                        name = user.nickname,
                        profileImageUrl = user.profileImageUrl.orEmpty(),
                        thumbnailImageUrl = user.thumbnailImageUrl.orEmpty()
                    )
                }
            }.toList()

            return routineTask.get().toDto(friends = friends as List<RoutineTaskFriendDto>)

        }
        throw IllegalArgumentException("task not found.")
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

    fun deleteById(taskId: String) {
        val today = LocalDate.now()
        val date = today.toDateString()
        val task = routineTaskRepository.findById(taskId)
        if (task.isPresent) {
            val targetUnitId = date + ":" + task.get().profileId + ":" + task.get().id
            routineTaskUnitRepository.deleteByUnitId(targetUnitId)
            routineTaskRepository.deleteById(taskId)
        }
    }

    fun inviteFriendToTask(taskId: String, friendId: String): RoutineTask {

        val task = routineTaskRepository.findById(taskId)

        if (task.isPresent) {
            val friend = routineTaskRepository.findByCodeAndProfileId(task.get().code!!, friendId)
            if (friend.isPresent) {
                throw Exception("Already invited friend");
            }

            val friendTask = RoutineTask(
                id = null,
                code = task.get().code,
                profileId = friendId,
                title = task.get().title,
//                timesOfWeek = task.get().timesOfWeek,
//                timesOfDay = task.get().timesOfDay,
                notify = task.get().notify,
                days = task.get().days,
                times = task.get().times,
                category = task.get().category,
                templateId = task.get().templateId,
                order = task.get().order,
                delayCount = task.get().delayCount
            )

            val routineTaskSaved = routineTaskRepository.save(friendTask)
            val routineTaskUnits = mutableListOf<RoutineTaskUnit>()
            getTodayRoutineTaskUnit(routineTaskUnits, routineTaskSaved)
            routineTaskUnitRepository.saveAll(routineTaskUnits)
            return routineTaskSaved
        } else {
            throw Exception("Task not exist")
        }
    }



}

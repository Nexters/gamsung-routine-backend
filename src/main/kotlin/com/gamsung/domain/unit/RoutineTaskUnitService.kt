package com.gamsung.domain.unit

import com.gamsung.api.dto.RoutineTaskFriendUnitDto
import com.gamsung.api.dto.RoutineTaskUnitDto
import com.gamsung.api.dto.toDto
import com.gamsung.api.dto.toEntity
import com.gamsung.domain.routine.RoutineTaskRepository
import com.gamsung.domain.security.AccountHolder
import com.gamsung.infra.toDateString
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskRepository: RoutineTaskRepository
) {
    private val WEEK_COUNT = 7

    fun createRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val date = LocalDateTime.now().toDateString()
        val id = date.plus(":").plus(routineTaskUnitDto.profileId).plus(":").plus(routineTaskUnitDto.taskId)
        val unit = RoutineTaskUnit.create(
            unitId = id,
            profileId = routineTaskUnitDto.profileId,
            date = date,
            localDate = LocalDate.now(),
            taskId = routineTaskUnitDto.taskId,
            title = routineTaskUnitDto.title,
            days = routineTaskUnitDto.days,
            times = routineTaskUnitDto.times,
            friendIds = null,
        )

        return routineTaskUnitRepository.save(unit)
    }

    fun searchRoutineTaskUnit(unitId: String): RoutineTaskUnit {
        return routineTaskUnitRepository.findById(unitId).get()
    }

    fun searchRoutineTaskUnitDay(profileId: String, date: LocalDate): List<RoutineTaskUnitDto> {
        val unitList = routineTaskUnitRepository.findAllByProfileIdAndLocalDate(profileId, date)
        val dtoList = mutableListOf<RoutineTaskUnitDto>()

        unitList.map {
            val friends = routineTaskUnitRepository.findAllByTaskIdAndLocalDate(it.taskId, date).map { unit ->
                RoutineTaskFriendUnitDto(
                    profileId = unit.profileId,
                    completeCount = unit.completeCount,
                    completedDateList = unit.completedDateList
                )
            }
            dtoList.add(it.toDto(friends))
        }
        return dtoList
    }

    fun updateRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val unit = routineTaskUnitRepository.findById(routineTaskUnitDto.id ?: "").get()
        return routineTaskUnitRepository.save(unit)
    }

    fun delayRoutineTaskUnit(unitId: String): String {
        // 해당 unit이 등록된 날짜를 확인
        val unit = routineTaskUnitRepository.findById(unitId).get()

        // 해당 unit의 태스크
        val taskDto = routineTaskRepository.findById(unit.taskId).get().toDto()

        // unit을 1회라도 수행하면 미루기 불가
        if (unit.completedDateList.size > 0)
            return "해당 태스크는 미룰 수 없습니다. (해당 Task 진행중)"

        // 일주일 매일 수행하는 task라면 바로 반환
        val planCount = unit.days?.size ?: WEEK_COUNT
        if (planCount > 6) {
            return "해당 태스크는 미룰 수 없습니다. (매일 수행)"
        }

        // 해당 날짜의 주차를 확인
        val date = unit.localDate
        val dayOfWeek = date.dayOfWeek.value // 월(1)...일(7)

        // 해당 날짜 이후에 미룰 수 있는 날짜가 있는지 확인
        val pastUnitList = mutableListOf<RoutineTaskUnit>()
        for (i in 0 until dayOfWeek) {
            val addDays = (i + 1).toLong()
            val dayUnit = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndLocalDateAAndDelayedDateTimeIsNull(
                unit.profileId, unit.taskId, date.plusDays(addDays)
            ).first()
            pastUnitList.add(dayUnit)
        }
        // 남은 날짜
        val remainDays = WEEK_COUNT - dayOfWeek
        // 남은 unit (오늘 Unit 포함) : 계획된 유닛 - 이미 지나간 유닛
        val remainUnitCount = planCount - pastUnitList.size

        if (remainDays <= remainUnitCount) {
            return "해당 태스크는 미룰 수 없습니다. (여유 일정 없음)"
        }

        // 태스크 delay count up
        taskDto.delayCount++
        routineTaskRepository.save(taskDto.toEntity())

        return "(๑>ᴗ<๑) 해당 태스크를 미뤘습니다."

//        // 미룰 수 있는 날짜 중 가장 가까운 날짜에 오늘 unit 배정
//        for (i in dayOfWeek until (WEEK_COUNT + 1)) {
//            // 계획에 포함되어 있지 않고
//            if (unit.days?.contains(i) == false) {
//                // 이미 밀린 태스크가 있지 않은 날짜 찾기
//                val newDate = date.plusDays(i.toLong())
//                val dayUnit = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndLocalDate(
//                    unit.profileId, unit.taskId, newDate
//                )
//                if (dayUnit.isEmpty()) {
//                    val newUnit = dayUnit.first().delay(
//                        newDate.toDateString(),
//                        newDate
//                    )
//                    routineTaskUnitRepository.save(newUnit)
//                    return "해당 태스크를 ${newDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)}로 미뤘습니다."
//                }
//            }
//        }
//        return "해당 태스크는 미룰 수 없습니다."
    }

    fun completeRoutineTaskUnit(taskId: String, date: String): Pair<RoutineTaskUnit, String> {
        val profile = AccountHolder.get()
        val unitId = "$date:${profile.id}:$taskId"
        val unit = routineTaskUnitRepository.findByUnitId(unitId).first()
        if (unit.completedDateList.size == unit.timesOfDay) {
            throw IllegalArgumentException("이미 오늘의 모든 태스크가 완료되었습니다.")
        }

        unit.complete(LocalDateTime.now())
        val saveUnit = routineTaskUnitRepository.save(unit)
        return Pair(saveUnit, "테스크가 완료되었습니다.")
    }

    fun checkCompleted(unitId: String): String {
        val unit = routineTaskUnitRepository.findByUnitId(unitId).first()
        if (unit.completedDateList.size == (unit.times?.size ?: -1)) {
            return "이미 오늘의 모든 태스크가 완료되었습니다."
        }
        return ""
    }

    fun backRoutineTaskUnit(taskId: String, date: String): Pair<RoutineTaskUnit, String> {
        val profile = AccountHolder.get()
        val unitId = "$date:${profile.id}:$taskId"

        val unit = routineTaskUnitRepository.findByUnitId(unitId).first()

        if (unit.completeCount < 1) {
            throw IllegalArgumentException("이미 모든 태스크가 되돌아갔습니다.")
        }

        return Pair(routineTaskUnitRepository.save(unit.back()), "태스크가 되돌아갔습니다.")
    }

    fun getRoutineTaskUnitAll(profileId: String): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileId(profileId)
    }

    fun getRoutineTaskUnit(
        profileId: String,
        fromDate: LocalDate,
        toDate: LocalDate
    ): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findAllByProfileIdAndLocalDateBetween(
            profileId, fromDate, toDate
        )
    }
}

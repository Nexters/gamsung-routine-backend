package com.gamsung.domain.unit

import com.gamsung.api.dto.RoutineTaskUnitDto
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {
    private val WEEK_COUNT = 7

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
            days = routineTaskUnitDto.days,
            times = routineTaskUnitDto.times,
            friendIds = null,
            completeCount = 0, // 시작은 0
        )

        return routineTaskUnitRepository.save(unit)
    }

    fun updateRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val unit = routineTaskUnitRepository.findById(routineTaskUnitDto.id ?: "").get()
        return routineTaskUnitRepository.save(unit)
    }

    fun delayRoutineTaskUnit(unitId: String): String {
        // 해당 unit이 등록된 날짜를 확인
        val unit = routineTaskUnitRepository.findById(unitId).get()

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
            val dayUnit = routineTaskUnitRepository.findByProfileIdAndTaskIdAndLocalDate(
                unit.profileId, unit.taskId, date.plusDays(addDays)
            ).first()
            pastUnitList.add(dayUnit)
        }
        // 남은 날짜
        val remainDays = WEEK_COUNT - dayOfWeek
        // 남은 unit (오늘 Unit 포함)
        val remainUnitCount = planCount - pastUnitList.size

        if (remainDays <= remainUnitCount) {
            return "해당 태스크는 미룰 수 없습니다. (여유 일정 없음)"
        }

        // 미룰 수 있는 날짜 중 가장 가까운 날짜에 오늘 unit 배정
        for (i in dayOfWeek until (WEEK_COUNT + 1)) {
            // 계획에 포함되어 있지 않고
            if (unit.days?.contains(i) == false) {
                // 이미 밀린 태스크가 있지 않은 날짜 찾기
                val newDate = date.plusDays(i.toLong())
                val dayUnit = routineTaskUnitRepository.findByProfileIdAndTaskIdAndLocalDate(
                    unit.profileId, unit.taskId, newDate
                )
                if (dayUnit.isEmpty()) {
                    val newUnit = dayUnit.first().update(
                        generateDate(newDate),
                        newDate
                    )
                    routineTaskUnitRepository.save(newUnit)
                    return "해당 태스크를 ${newDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)}로 미뤘습니다."
                }
            }
        }
        return "해당 태스크는 미룰 수 없습니다."
    }

    fun completeRoutineTaskUnit(unitId: String): Pair<RoutineTaskUnit, String> {
        val unit = routineTaskUnitRepository.findById(unitId).get()
        val message = checkCompleted(unitId)
        return if (message.isBlank()) {
            unit.complete(LocalDateTime.now())
            val saveUnit = routineTaskUnitRepository.save(unit)
            Pair(saveUnit, message)
        } else {
            Pair(unit, message)
        }
    }

    fun checkCompleted(unitId: String): String {
        val unit = routineTaskUnitRepository.findById(unitId).get()
        if (unit.completedDateList.size == (unit.times?.size ?: -1)) {
            return "이미 완료된 태스크 입니다."
        }
        return ""
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

    private fun generateDate(currDate: LocalDate): String {
        val monthString = currDate.month.value.toString()
        val month = if (monthString.length < 2) ("0$monthString") else monthString

        val dayString = currDate.dayOfMonth.toString()
        val day = if (dayString.length < 2) ("0$dayString") else dayString

        return currDate.year.toString() + month + day
    }

}

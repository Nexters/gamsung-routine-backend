package com.gamsung.domain.unit

import com.gamsung.api.dto.RoutineTaskUnitDto
import com.gamsung.api.dto.toDto
import com.gamsung.api.dto.toEntity
import com.gamsung.domain.auth.service.Account
import com.gamsung.domain.auth.service.SocialType
import com.gamsung.domain.routine.RoutineTaskRepository
import com.gamsung.domain.security.AccountHolder
import com.gamsung.infra.toDateString
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskRepository: RoutineTaskRepository
) {
    private val DELAY_STATUS_CHECK = 1
    private val DELAY_STATUS_DELAY = 2

    private val DELAY_AVAILABLE = true
    private val DELAY_NOT_AVAILABLE = false

    private val WEEK_COUNT = 7

    fun createRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val date = LocalDateTime.now()
        val dateString = date.toDateString()
        val id = dateString.plus(":").plus(routineTaskUnitDto.profileId).plus(":").plus(routineTaskUnitDto.taskId)
        val unit = RoutineTaskUnit.create(
            id = id,
            profileId = routineTaskUnitDto.profileId,
            date = dateString,
            localDate = date.plusHours(9).toLocalDate(),
            taskId = routineTaskUnitDto.taskId,
            taskCode = routineTaskUnitDto.taskCode,
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

//    fun searchRoutineTaskUnitDay(profileId: String, date: LocalDate): List<RoutineTaskUnitDto> {
//        val unitList = routineTaskUnitRepository.findAllByProfileIdAndLocalDateAndDelayedDateTimeIsNull(profileId, date)
//        val dtoList = mutableListOf<RoutineTaskUnitDto>()
//
//        unitList.map {
//            val friends = routineTaskUnitRepository.findAllByTaskIdAndLocalDateAndDelayedDateTimeIsNull(it.taskId, date).map { unit ->
//                RoutineTaskFriendUnitDto(
//                    profileId = unit.profileId,
//                    completeCount = unit.completeCount,
//                    completedDateList = unit.completedDateList
//                )
//            }
//            dtoList.add(it.toDto(friends))
//        }
//        }
//        }
//        return dtoList
//    }

    // ????????? ??? ???! ????????? ???????????? ????????? ?????? ?????? ??? ??????
//    fun searchRoutineTaskUnitPeriod(
//        profileId: String,
//        taskId: String,
//        fromDate: LocalDate,
//        toDate: LocalDate
//    ): List<RoutineTaskUnitDto> {
//        val unitList = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndLocalDateBetweenAndDelayedDateTimeIsNull(
//            profileId, taskId, fromDate, toDate
//        )
//        val dtoList = mutableListOf<RoutineTaskUnitDto>()
//
//        unitList.map {
//            val friends = routineTaskUnitRepository.findAllByTaskIdAndLocalDateAndDelayedDateTimeIsNull(it.taskId, it.localDate)
//                .map { unit ->
//                    RoutineTaskFriendUnitDto(
//                        profileId = unit.profileId,
//                        completeCount = unit.completeCount,
//                        completedDateList = unit.completedDateList
//                    )
//                }
//            dtoList.add(it.toDto(friends))
//        }
//        return dtoList
//    }

    fun updateRoutineTaskUnit(routineTaskUnitDto: RoutineTaskUnitDto): RoutineTaskUnit {
        val unit = routineTaskUnitRepository.findById(routineTaskUnitDto.id ?: "").get()
        return routineTaskUnitRepository.save(unit)
    }

    fun delayRoutineTaskUnit(taskId: String, status: Int): Pair<Boolean, String> {

        val nowDateTime = LocalDateTime.now()
        val profile = AccountHolder.get()
//        val profile = Account(
//            id = "610440cca49e190b7a79c112",
//            socialType = SocialType.KAKAO,
//            nickname = "",
//            email = "",
//            profileImageUrl = "",
//            thumbnailImageUrl = "",
//            pushNotification = true
//        )

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val unit = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndDateAndDelayedDateTimeIsNull(
            profile.id, taskId, nowDateTime.format(formatter)
        ).firstOrNull() ?: return Pair(DELAY_NOT_AVAILABLE, "?????? ???????????? ?????? ??? ????????????. (?????? Task ??????)") // unit??? 1????????? ???????????? ????????? ??????

        if (unit.completedDateList.size > 0)
            return Pair(DELAY_NOT_AVAILABLE, "?????? ???????????? ?????? ??? ????????????. (?????? Task ?????????)")

        // ?????? unit??? ?????????
        val taskDto = routineTaskRepository.findById(unit.taskId).get().toDto()

        // ????????? ?????? ???????????? task?????? ?????? ??????
        val planCount = unit.days?.size ?: WEEK_COUNT
        if (planCount > 6) {
            return Pair(DELAY_NOT_AVAILABLE, "?????? ???????????? ?????? ??? ????????????. (?????? ??????)")
        }

        // ?????? ????????? ????????? ??????
//        val unitDate = unit.localDate
        val dayOfWeek = nowDateTime.dayOfWeek.value // ???(1)...???(7)

        // ?????? ?????? ????????? ?????? ??? ?????? ????????? ????????? ??????
        val pastUnitList = mutableListOf<RoutineTaskUnit>()
        for (i in 0 until dayOfWeek) {
            val addDays = (i + 1).toLong()
            val addedDate = nowDateTime.plusDays(addDays)
            val dayUnit = routineTaskUnitRepository.findAllByProfileIdAndTaskIdAndDateAndDelayedDateTimeIsNull(
                unit.profileId, unit.taskId, addedDate.format(formatter)
            ).firstOrNull()
            dayUnit?.let { pastUnitList.add(it) }
        }
        // ?????? ?????? : ????????? ????????? ???????????? ????????? ??? ?????? ??????
        val remainDays = WEEK_COUNT - dayOfWeek
        // ?????? unit (?????? Unit ??????) : ????????? ?????? - ?????? ????????? ??????
        val remainUnits = unit.days?.filter { it > dayOfWeek }?.map { it }?.sorted()
        val remainUnitCount = remainUnits?.size ?: 0

        if (remainDays - remainUnitCount - taskDto.delayCount <= 0) {
            return Pair(DELAY_NOT_AVAILABLE, "?????? ???????????? ?????? ??? ????????????. (?????? ?????? ??????)")
        }

        var delayDay: LocalDateTime? = null
        // Unit delay ??????
        unit.delayedDateTime = nowDateTime

        val noPlanDays = mutableListOf<Int>()
        var tempDelayCount = taskDto.delayCount
        for (i in dayOfWeek + 1..WEEK_COUNT) {
            if (remainUnits?.contains(i) == false) {
                if (tempDelayCount > 0) {
                    tempDelayCount--
                } else {
                    noPlanDays.add(i)
                }
            }
        }

        val delayAddNumber = noPlanDays[0] - dayOfWeek
        delayDay = nowDateTime.plusDays(delayAddNumber.toLong())

        if (status == DELAY_STATUS_DELAY) {
            routineTaskUnitRepository.save(unit)
            // ????????? delay count up
            taskDto.delayCount++
            routineTaskRepository.save(taskDto.toEntity())
        }

        return Pair(
            DELAY_AVAILABLE,
            "?????? ???????????? ${delayDay?.dayOfWeek?.getDisplayName(TextStyle.FULL, Locale.KOREAN)}??? ???????????????."
        )
    }

    fun completeRoutineTaskUnit(taskId: String, date: String): Pair<RoutineTaskUnit, String> {
        val profile = AccountHolder.get()
        val unitId = "$date:${profile.id}:$taskId"
        val unit = routineTaskUnitRepository.findByIdAndDelayedDateTimeIsNull(unitId).first()
        if (unit.completedDateList.size == unit.timesOfDay) {
            throw IllegalArgumentException("?????? ????????? ?????? ???????????? ?????????????????????.")
        }

        unit.complete(LocalDateTime.now())
        val saveUnit = routineTaskUnitRepository.save(unit)
        return Pair(saveUnit, "???????????? ?????????????????????.")
    }

    fun checkCompleted(unitId: String): String {
        val unit = routineTaskUnitRepository.findByIdAndDelayedDateTimeIsNull(unitId).first()
        if (unit.completedDateList.size == (unit.times?.size ?: -1)) {
            return "?????? ????????? ?????? ???????????? ?????????????????????."
        }
        return ""
    }

    fun backRoutineTaskUnit(taskId: String, date: String): Pair<RoutineTaskUnit, String> {
        val profile = AccountHolder.get()
        val unitId = "$date:${profile.id}:$taskId"

        val unit = routineTaskUnitRepository.findByIdAndDelayedDateTimeIsNull(unitId).first()

        if (unit.completeCount < 1) {
            throw IllegalArgumentException("?????? ?????? ???????????? ?????????????????????.")
        }

        return Pair(routineTaskUnitRepository.save(unit.back()), "???????????? ?????????????????????.")
    }

    fun getRoutineTaskUnitAll(profileId: String): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findByProfileId(profileId)
    }

    fun getRoutineTaskUnit(
        profileId: String,
        fromDate: LocalDate,
        toDate: LocalDate
    ): MutableList<RoutineTaskUnit> {
        return routineTaskUnitRepository.findAllByProfileIdAndLocalDateBetweenAndDelayedDateTimeIsNull(
            profileId, fromDate, toDate
        )
    }
}

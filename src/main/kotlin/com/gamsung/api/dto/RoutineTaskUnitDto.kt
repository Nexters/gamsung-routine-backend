package com.gamsung.api.dto

import com.gamsung.domain.unit.RoutineTaskUnit
import java.time.LocalDate
import java.time.LocalDateTime


data class RoutineTaskUnitDto(
    val id: String?,
    val profileId: String,
    val date: String?,
    val localDate: LocalDate?,
    val taskId: String,
    val taskCode: String,
    val title: String,
    val days: List<Int>?,
    val times: List<String>?,
    val friends: List<RoutineTaskFriendUnitDto>?,
//    val completeCount: Int,
    val completedDateList: List<LocalDateTime>
) {
    val timesOfDay: Int get() = this.times?.size ?: 0
    val timesOfWeek: Int get() = this.days?.size ?: 0
}

fun RoutineTaskUnit.toDto() = RoutineTaskUnitDto(
    id = id,
    profileId = profileId,
    date = date,
    localDate = localDate,
    taskId = taskId,
    taskCode = taskCode,
    title = title,
    days = days,
    times = times,
    friends = emptyList(),
//    completeCount = completeCount,
    completedDateList = completedDateList
)

fun RoutineTaskUnit.toDto(friends: List<RoutineTaskFriendUnitDto>) = RoutineTaskUnitDto(
    id = id,
    profileId = profileId,
    date = date,
    localDate = localDate,
    taskId = taskId,
    taskCode = taskCode,
    title = title,
    days = days,
    times = times,
    friends = friends,
//    completeCount = completeCount,
    completedDateList = completedDateList
)

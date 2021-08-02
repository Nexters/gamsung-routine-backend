package com.gamsung.api.dto

import com.gamsung.domain.unit.RoutineTaskUnit
import java.time.LocalDate


data class RoutineTaskUnitDto(
    val id: String?,
    val profileId: String,
    val date: String,
    val localDate: LocalDate,
    val taskId: String,
    val title: String,
    val timesOfDay: Int,
    val days: List<Int>?,
    val times: List<String>?,
    val friends: List<RoutineTaskFriendUnitDto>?,
    val completeCount: Int,
    val completedDateList: List<LocalDate>
)

fun RoutineTaskUnit.toDto(friends: List<RoutineTaskFriendUnitDto>) = RoutineTaskUnitDto(
    id = id,
    profileId = profileId,
    date = date,
    localDate = localDate,
    taskId = taskId,
    title = title,
    timesOfDay = timesOfDay,
    days = days,
    times = times,
    friends = friends,
    completeCount = completeCount,
    completedDateList = completedDateList
)

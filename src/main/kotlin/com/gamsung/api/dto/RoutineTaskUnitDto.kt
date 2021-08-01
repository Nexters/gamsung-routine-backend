package com.gamsung.api.dto

import com.gamsung.domain.unit.RoutineTaskUnit
import java.time.LocalDate

data class RoutineTaskUnitDto(
    val profileId: String,
    val date: String,
//    val localDate: LocalDate,
    val taskId: String,
    val title: String,

    val timesOfDay: Int,
    val days: List<Int>?,
    val times: List<String>?,

    val friendIds: List<String>?,
    val completeCount: Int,
//    val completedDateList: List<LocalDate>
)

fun RoutineTaskUnit.toDto() = RoutineTaskUnitDto(
    profileId = profileId,
    date = date,
//    localDate = localDate,
    taskId = taskId,
    title = title,
    timesOfDay = timesOfDay,
    days = days,
    times = times,
    friendIds = friendIds,
    completeCount = completeCount,
//    completedDateList = completedDateList
)
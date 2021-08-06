package com.gamsung.api.dto

import com.gamsung.domain.routine.RoutineTask

/**
 * @author Jongkook
 * @date : 2021/07/10
 */
data class RoutineTaskDto(
    val id: String, // UUID
    val profileId: String,
    val title: String,
//    var timesOfWeek: Int, // 주당 횟수,
//    var timesOfDay: Int, // 1일 횟수,
    var notify: Boolean, // 알람 여부
    val days: List<Int> = emptyList(), // 월 수 금
    val times: List<String> = emptyList(), // 09:00, 10:00
    val category: String, // Category. 아마도 Enum?
    val templateId: String?, // UUID
    val order: Int, // 나열 순서
)

fun RoutineTask.toDto() =
    RoutineTaskDto(
        id = id,
        profileId = profileId,
        title = title,
//        timesOfWeek = timesOfWeek,
//        timesOfDay = timesOfDay,
        notify = notify,
        days = days,
        times = times,
        category = category,
        templateId = templateId,
        order = order
    )

fun RoutineTaskDto.toEntity() =
    RoutineTask(
        id = id,
        profileId = profileId,
        title = title,
//        timesOfWeek = timesOfWeek,
//        timesOfDay = timesOfDay,
        notify = notify,
        days = days ?: emptyList(),
        times = times ?: emptyList(),
        category = category,
        templateId = templateId ?: "",
        order = order
    )

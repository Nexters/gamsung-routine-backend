package com.gamsung.api.dto

import com.gamsung.domain.routine.RoutineTask

/**
 * @author Jongkook
 * @date : 2021/07/10
 * .
 * done 여부는 history에서 체크
 */
data class RoutineTaskDto(
    var id: String, // UUID
    var profileId: String,
    var title: String,
    var timesOfWeek: Int, // 주당 횟수
    var timesOfDay: Int, // 1일 횟수
    var notify: Boolean, // 알람 여부
    var days: List<Int>, // 월 수 금
    var times: List<String>, // 09:00, 10:00
    var category: String, // Category. 아마도 Enum?
    var templateId: String, // UUID
    var order: Int, // 나열 순서
)

fun RoutineTask.toDto() =
    RoutineTaskDto(
        id = id,
        profileId = profileId,
        title = title,
        timesOfWeek = timesOfWeek,
        timesOfDay = timesOfDay,
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
        timesOfWeek = timesOfWeek,
        timesOfDay = timesOfDay,
        notify = notify,
        days = days,
        times = times,
        category = category,
        templateId = templateId,
        order = order
    )

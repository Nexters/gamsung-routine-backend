package com.gamsung.api.dto

import com.gamsung.domain.routine.RoutineTask

/**
 * @author Jongkook
 * @date : 2021/07/10
 */
data class RoutineTaskDto(
    val id: String?, // UUID
    val taskId: String?,
    val profileId: String,
    val title: String,
    var notify: Boolean, // 알람 여부
    val days: List<Int> = emptyList(), // 월 수 금
    val times: List<String> = emptyList(), // 09:00, 10:00
    val category: String?, // Category. 아마도 Enum?
    val templateId: String?, // UUID
    val order: Int, // 나열 순서
    var delayCount: Int, // 미루기 한 횟수, init = 0
)

fun RoutineTask.toDto() =
    RoutineTaskDto(
        id = id,
        taskId = taskId,
        profileId = profileId,
        title = title,
        notify = notify,
        days = days,
        times = times,
        category = category,
        templateId = templateId,
        order = order,
        delayCount = delayCount
    )


fun RoutineTaskDto.toNewEntity() =
    RoutineTask(
        id = id,
        taskId = profileId + System.currentTimeMillis(),
        profileId = profileId,
        title = title,
        notify = notify,
        days = days,
        times = times,
        category = category ?: "",
        templateId = templateId ?: "",
        order = order,
        delayCount = 0
    )

fun RoutineTaskDto.toEntity() =
    RoutineTask(
        id = id,
        taskId = taskId,
        profileId = profileId,
        title = title,
        notify = notify,
        days = days,
        times = times,
        category = category ?: "",
        templateId = templateId ?: "",
        order = order,
        delayCount = delayCount
    )

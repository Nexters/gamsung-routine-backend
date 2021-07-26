package com.gamsung.api.dto

import java.time.LocalDateTime

data class RoutineHistoryDto (
    val id: Long,
    val profileId: Long,
    val taskId: Long,
    val completedAt: LocalDateTime
)

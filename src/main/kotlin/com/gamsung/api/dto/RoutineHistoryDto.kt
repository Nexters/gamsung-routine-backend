package com.gamsung.api.dto

import java.time.LocalDateTime

data class RoutineHistoryDto (
    var id: Long,
    var profileId: Long,
    var taskId: Long,
    var completedAt: LocalDateTime
)

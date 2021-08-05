package com.gamsung.api.dto

import java.time.LocalDateTime

data class RoutineTaskFriendUnitDto(
    val profileId: String,
    val completeCount: Int,
    val completedDateList: List<LocalDateTime>
)

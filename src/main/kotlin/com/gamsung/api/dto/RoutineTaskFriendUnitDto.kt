package com.gamsung.api.dto

import java.time.LocalDateTime

data class RoutineTaskFriendUnitDto(
    val taskId: String,
    val profileId: String,
    val name: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    val completeCount: Int,
    val completedDateList: List<LocalDateTime>
)

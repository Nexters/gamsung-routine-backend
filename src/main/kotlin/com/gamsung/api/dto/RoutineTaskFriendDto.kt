package com.gamsung.api.dto

import java.time.LocalDateTime

data class RoutineTaskFriendDto(
    val taskId: String,
    val profileId: String,
    val name: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String
)

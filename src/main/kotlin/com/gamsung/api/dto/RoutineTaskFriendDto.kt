package com.gamsung.api.dto

data class RoutineTaskFriendDto(
    val taskId: String,
    val profileId: String,
    val name: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String
)

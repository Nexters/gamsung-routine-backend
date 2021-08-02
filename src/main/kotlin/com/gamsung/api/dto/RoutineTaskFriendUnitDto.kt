package com.gamsung.api.dto

import java.time.LocalDate

data class RoutineTaskFriendUnitDto(
    val profileId: String,
    val completeCount: Int,
    val completedDateList: List<LocalDate>
)

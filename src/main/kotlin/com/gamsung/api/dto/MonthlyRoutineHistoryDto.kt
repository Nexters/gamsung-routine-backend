package com.gamsung.api.dto

import com.gamsung.domain.unit.RoutineTaskUnit

data class MonthlyRoutineHistoryDto (
    val year: Int,
    val month: Int,
    val dailyRoutines: Map<String, List<RoutineTaskUnit>>
)

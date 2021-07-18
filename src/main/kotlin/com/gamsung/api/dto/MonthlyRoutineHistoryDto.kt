package com.gamsung.api.dto

import com.gamsung.domain.routine.RoutineTaskHistory

data class MonthlyRoutineHistoryDto (
    val year: Int,
    val month: Int,
    val dailyRoutineHistory: Map<Int, List<RoutineTaskHistory>>
)

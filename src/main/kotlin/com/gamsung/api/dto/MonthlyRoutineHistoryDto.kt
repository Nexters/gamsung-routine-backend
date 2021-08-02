package com.gamsung.api.dto

data class MonthlyRoutineHistoryDto (
    val year: Int,
    val month: Int,
    val dailyRoutines: Map<String, List<RoutineTaskUnitDto>>
)

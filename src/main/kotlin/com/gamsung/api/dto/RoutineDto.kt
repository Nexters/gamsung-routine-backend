package com.gamsung.api.dto

import com.gamsung.domain.routine.RoutineTaskHistory

/**
 * @author Jongkook
 * @date : 2021/07/10
 */

data class RoutineDto(
    var routine: Map<Int, List<RoutineTaskDto>>
)

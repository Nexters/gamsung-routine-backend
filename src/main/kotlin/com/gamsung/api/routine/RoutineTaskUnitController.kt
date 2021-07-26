package com.gamsung.api.routine

import com.gamsung.domain.routine.RoutineTaskUnitService
import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routine/unit")
class RoutineTaskUnitController(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskUnitService: RoutineTaskUnitService
) {

}

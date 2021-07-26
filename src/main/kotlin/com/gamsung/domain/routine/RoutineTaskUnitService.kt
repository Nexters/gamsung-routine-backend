package com.gamsung.domain.routine

import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.stereotype.Service

@Service
class RoutineTaskUnitService(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
) {

}

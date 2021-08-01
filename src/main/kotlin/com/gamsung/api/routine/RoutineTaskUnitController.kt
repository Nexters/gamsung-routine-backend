package com.gamsung.api.routine

import com.gamsung.domain.routine.RoutineTask
import com.gamsung.domain.routine.RoutineTaskUnit
import com.gamsung.domain.routine.RoutineTaskUnitService
import com.gamsung.repository.RoutineTaskUnitRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/routine/unit")
class RoutineTaskUnitController(
    private val routineTaskUnitRepository: RoutineTaskUnitRepository,
    private val routineTaskUnitService: RoutineTaskUnitService
) {

    @PostMapping
    fun create(
        @RequestBody routineTask: RoutineTask
    ): RoutineTaskUnit {
        val date = LocalDate.now() // todo
        return routineTaskUnitService.createRoutineTaskUnit(routineTask, date)
    }
/*
    @PutMapping
    fun update(
        @RequestBody routineTaskDto: RoutineTaskDto
    ): RoutineTaskDto {
        // task id를 체크 --> todo: message를 어떻게 노출시킬지 고민 중
        // to 상환 : orElseThrow =--> ?: throw
        val exist = routineTaskRepository.findById(routineTaskDto.id)
        if (exist.isEmpty) throw IllegalArgumentException("업데이트 할 Task를 찾을 수 없습니다.")

        val routineTask = routineTaskRepository.save(routineTaskDto.toEntity())
        return routineTask.toDto()
    }

    @DeleteMapping("/{taskId}")
    fun delete(@PathVariable taskId: String) {
        routineTaskRepository.deleteById(taskId)
    }
*/
}
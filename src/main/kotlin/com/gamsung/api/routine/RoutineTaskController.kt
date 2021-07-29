package com.gamsung.api.routine

import com.gamsung.api.dto.*
import com.gamsung.domain.routine.RoutineTaskService
import com.gamsung.repository.RoutineTaskRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routine")
class RoutineTaskController(
    private val routineTaskRepository: RoutineTaskRepository,
    private val routineTaskService: RoutineTaskService
) {

    @PostMapping
    fun create(
        @RequestBody routineTaskDto: RoutineTaskDto
    ): RoutineTaskDto {
        return routineTaskRepository.save(routineTaskDto.toEntity()).toDto()
    }

//    @GetMapping("/{profileId}")
//    fun read(@PathVariable profileId: String): RoutineDto {
//        return routineTaskService.getUserRoutines(profileId)
//    }

    @GetMapping("/weekly/{profileId}")
    fun read(@PathVariable profileId: String,
             @RequestParam year: Int?,
             @RequestParam month: Int?): MonthlyRoutineHistoryDto {
        return routineTaskService.getMonthlyRoutines(profileId, year, month)
    }

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

}

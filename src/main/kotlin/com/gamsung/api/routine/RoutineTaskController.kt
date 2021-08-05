package com.gamsung.api.routine

import com.gamsung.api.BusinessException
import com.gamsung.api.dto.*
import com.gamsung.domain.routine.RoutineTaskRepository
import com.gamsung.domain.routine.RoutineTaskService
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/routine")
class RoutineTaskController(
    private val routineTaskRepository: RoutineTaskRepository,
    private val routineTaskService: RoutineTaskService
) {

    @PostMapping
    fun create(
        @RequestBody routineTaskDto: RoutineTaskDto
    ): ResponseDto<RoutineTaskDto?> {
        if ((routineTaskDto.days?.isEmpty() != false) || (routineTaskDto.times?.isEmpty() != false)) {
            throw BusinessException("days과 times는 1개 이상의 값이 있어야 합니다.")
        }

        return ResponseDto.ok(
            routineTaskRepository.save(routineTaskDto.toEntity()).toDto()
        )
    }

    // 권사원 코멘트 : nullable로 받을 수 있는 값은 @RequestParam 에 required=false
    @GetMapping("/monthly/{profileId}")
    fun read(
        @PathVariable profileId: String,
        @RequestParam year: Int,
        @RequestParam month: Int
    ): MonthlyRoutineHistoryDto {
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

    @GetMapping("/invite/{taskId}/{friendId}")
    fun inviteFriend(
        @PathVariable taskId: String,
        @PathVariable friendId: String
    ): RoutineTaskDto {
        return routineTaskService.inviteFriendToTask(taskId, friendId).toDto()
    }

}

package com.gamsung.api.routine

import com.gamsung.api.BusinessException
import com.gamsung.api.dto.*
import com.gamsung.domain.routine.RoutineTaskRepository
import com.gamsung.domain.routine.RoutineTaskService
import com.google.api.client.util.DateTime
import io.swagger.annotations.ApiOperation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat

@RestController
@RequestMapping("/api/routine")
class RoutineTaskController(
    private val routineTaskRepository: RoutineTaskRepository,
    private val routineTaskService: RoutineTaskService
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(RoutineTaskController::class.java)
    }

    @ApiOperation(value = "멀티 Task 생성")
    @PostMapping("/multi")
    fun createMany(
        @RequestBody routineTaskDtos: List<RoutineTaskDto>
    ): ResponseDto<List<RoutineTaskDto>> {
        return ResponseDto.ok(
            routineTaskRepository.saveAll(routineTaskDtos.map { it.toNewEntity() }).map { it.toDto() }
        )
    }

    @ApiOperation(value = "단일 Task 생성")
    @PostMapping
    fun create(
        @RequestBody routineTaskDto: RoutineTaskDto
    ): ResponseDto<RoutineTaskDto?> {
        require(routineTaskDto.days.isNotEmpty() && routineTaskDto.times.isNotEmpty()) {
            log.warn("days size  : ${routineTaskDto.days.size}")
            log.warn("times size : ${routineTaskDto.times.size}")
            throw BusinessException("days과 times는 1개 이상의 값이 있어야 합니다.")
        }

        try {
            val format = SimpleDateFormat("HH:mm")
            routineTaskDto.times.forEach {
                val date = format.parse(it)
                val text = format.format(date)
                require(text == it) {
                    throw BusinessException("Time의 포맷이 맞지 않습니다. (Range issue)")
                }
            }
        } catch (e: Exception) {
            throw BusinessException("Time의 포맷이 맞지 않습니다. (Unparseable date)")
        }

        return ResponseDto.ok(
            routineTaskService.save(routineTaskDto).toDto()
        )
    }

    @ApiOperation(value = "단일 Task 조회")
    @GetMapping("/{id}")
    fun read(@PathVariable id: String): RoutineTaskDto {
        return routineTaskService.getRoutineTask(id)
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
        val exist = routineTaskRepository.findById(routineTaskDto.id ?: "")
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

    @DeleteMapping("/leave/{taskId}/{friendId}")
    fun leaveFriend(
        @PathVariable taskId: String,
        @PathVariable friendId: String
    ) {
        routineTaskService.leaveFriendToTask(taskId, friendId)
    }


}

package com.gamsung.api.unit

import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.dto.RoutineTaskUnitDto
import com.gamsung.domain.unit.RoutineTaskUnitService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routine/unit")
class RoutineTaskUnitController(
    private val routineTaskUnitService: RoutineTaskUnitService
) {

    @ApiOperation(value = "Task unit 생성")
    @PostMapping("")
    fun create(
        @RequestBody routineTaskUnitDto: RoutineTaskUnitDto
    ): ResponseDto<String> {
        val unit = routineTaskUnitService.createRoutineTaskUnit(routineTaskUnitDto)
        return ResponseDto.ok(
            unit.id ?: "ID를 찾을 수 없습니다"
        )
    }

    @ApiOperation(value = "단일 Task unit 수정")
    @PutMapping("")
    fun update(@RequestBody routineTaskUnitDto: RoutineTaskUnitDto): ResponseDto<String> {
        val unit = routineTaskUnitService.updateRoutineTaskUnit(routineTaskUnitDto)
        return ResponseDto.ok(
            unit.id ?: "ID를 찾을 수 없습니다"
        )
    }

    @ApiOperation(value = "단일 Task unit 미루기")
    @PatchMapping("/delay/{unitId}")
    fun delay(@PathVariable unitId: String): ResponseDto<String> {
        val message = routineTaskUnitService.delayRoutineTaskUnit(unitId)
        return ResponseDto.ok(message)
    }

//    @ApiOperation(value = "Task unit 일괄 수정")

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

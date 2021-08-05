package com.gamsung.api.unit

import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.dto.RoutineTaskUnitDto
import com.gamsung.api.dto.toDto
import com.gamsung.domain.unit.RoutineTaskUnitService
import io.swagger.annotations.ApiOperation
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

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

    @ApiOperation(value = "단일 Task unit 조회")
    @GetMapping("/{unitId}")
    fun searchUnit(
        @PathVariable unitId: String
    ): ResponseDto<RoutineTaskUnitDto?> {
        return ResponseDto.ok(
            routineTaskUnitService.searchRoutineTaskUnit(unitId).toDto()
        )
    }

    @ApiOperation(value = "특정일 Task unit 조회")
    @GetMapping("/day/{profileId}")
    fun searchUnitDay(
        @PathVariable profileId: String,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate,
    ): ResponseDto<List<RoutineTaskUnitDto>> {
        return ResponseDto.ok(
            routineTaskUnitService.searchRoutineTaskUnitDay(profileId, date)
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

    @ApiOperation(value = "Task 1회 완료하기")
    @PatchMapping("/complete/{unitId}")
    fun complete(@PathVariable unitId: String): ResponseDto<RoutineTaskUnitDto?> {
        val pair = routineTaskUnitService.completeRoutineTaskUnit(unitId)

        return ResponseDto.ok(
            message = pair.second,
            data = pair.first.toDto()
        )
    }

    @ApiOperation(value = "Task 1회 되돌리기")
    @PatchMapping("/back/{unitId}")
    fun back(@PathVariable unitId: String): ResponseDto<RoutineTaskUnitDto?> {
        val pair = routineTaskUnitService.backRoutineTaskUnit(unitId)

        return ResponseDto.ok(
            message = pair.second,
            data = pair.first.toDto()
        )
    }

//    @ApiOperation(value = "Task unit 일괄 수정")

/*
    @DeleteMapping("/{taskId}")
    fun delete(@PathVariable taskId: String) {
        routineTaskRepository.deleteById(taskId)
    }
*/
}

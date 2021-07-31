package com.gamsung.api.template

import com.gamsung.api.dto.CategoryDto
import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.dto.TemplateDto
import com.gamsung.api.dto.toDto
import com.gamsung.domain.template.TemplateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateController(
    private val templateService: TemplateService
) {
    @GetMapping("/api/v1/category")
    fun getCategory(): ResponseDto<List<CategoryDto>> {
        val categories = templateService.getCategory()
        return ResponseDto.ok(categories.toDto())
    }

    @GetMapping("/api/v1/category/{categoryId}/template")
    fun getTemplates(
        @PathVariable categoryId: String,
    ): ResponseDto<List<TemplateDto>> {
        val templates = templateService.get(categoryId)
        return ResponseDto.ok(
            templates.toDto()
        )
    }
}

package com.gamsung.api.dto

import com.gamsung.domain.template.Category
import com.gamsung.domain.template.Template
import com.gamsung.domain.template.TemplateTask

/**
 * @author Jongkook
 * @date : 2021/07/10
 */
data class TemplateDto(
    val id: String,
    val name: String,
    val tasks: List<TemplateTaskDto>,
    val templateIconUrl: String?,
)

data class TemplateTaskDto(
    val id: String,
    val name: String,
)

data class CategoryDto(
    val id: String,
    val name: String,
)

@JvmName("toDtoCategory")
fun List<Category>.toDto() = this.map {
    CategoryDto(id = it.id, name = it.name)
}

fun List<Template>.toDto() = this.map {
    TemplateDto(
        id = it.id,
        name = it.name,
        tasks = it.tasks.map(TemplateTask::toDto),
        templateIconUrl = it.templateIconUrl
    )
}

fun TemplateTask.toDto() = TemplateTaskDto(
    id = id,
    name = name,
)

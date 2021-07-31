package com.gamsung.domain.template

data class Category(
    val id: String,
    val name: String,
)

data class Template(
    val id: String,
    val categoryId: String,
    val name: String,
    val templateIconUrl: String? = null,
    val tasks: List<TemplateTask>,
)

data class TemplateTask(
    val id: String,
    val name: String,
    val defaultDays: List<Int>,
    val dailyTimes: Int = 1,
)

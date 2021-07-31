package com.gamsung.domain.template

import org.springframework.stereotype.Service

@Service
class TemplateService {
    fun getCategory(): List<Category> {
        return MOCK_CATEGORY
    }

    fun get(categoryId: String?): List<Template> {
        val templates = MOCK_TEMPLATE
        return if (categoryId != null) {
            templates.filter { it.categoryId == categoryId }
        } else {
            templates
        }
    }

    companion object {
        private val MOCK_CATEGORY = listOf(
            Category("1", "건강"),
            Category("2", "학습"),
            Category("3", "인플루언스"),
            Category("4", "취미"),
        )

        private val MOCK_TEMPLATE = listOf(
            Template(
                id = "1", categoryId = "1", name = "살을 빼고 싶어", tasks = listOf(
                    TemplateTask(
                        id = "1", name = "물 마시기", defaultDays = listOf(1, 2, 3, 4, 5, 6, 7)
                    ),
                    TemplateTask(
                        id = "2", name = "물 마시기", defaultDays = listOf(1, 2, 3, 4, 5, 6, 7)
                    ),
                )
            ),
            Template(
                id = "2", categoryId = "1", name = "살을 빼고 싶어", tasks = listOf(
                    TemplateTask(
                        id = "3", name = "물 마시기", defaultDays = listOf(1, 2, 3, 4, 5, 6, 7),
                    ),
                    TemplateTask(
                        id = "4", name = "물 마시기", defaultDays = listOf(1, 2, 3, 4, 5, 6, 7),
                    ),
                )
            )
        )
    }
}

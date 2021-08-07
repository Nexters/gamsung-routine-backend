package com.gamsung.domain.template

import com.gamsung.infra.getOrStringEmpty
import com.gamsung.infra.getOrStringZero
import com.gamsung.infra.google.GoogleSheetTemplate
import com.gamsung.infra.ifEmptyToNull
import com.gamsung.infra.toValueList
import org.springframework.stereotype.Service

@Service
class TemplateService(
    private val googleSheetTemplate: GoogleSheetTemplate
) {
    fun getCategory(): List<Category> {
        val categoryValueRange =
            googleSheetTemplate.getSheet("1f6wt7lzhaTGIvd5--4sINJAMQSzk3w27P3scwxamPMM", "Category")
        val values = categoryValueRange.toValueList()

        return values.map { Category(it.getOrStringZero(0), it.getOrStringEmpty(1)) }
    }

    fun get(categoryId: String?): List<Template> {
        val templateValueRange =
            googleSheetTemplate.getSheet("1f6wt7lzhaTGIvd5--4sINJAMQSzk3w27P3scwxamPMM", "Template")
        val templateTaskValueRange =
            googleSheetTemplate.getSheet("1f6wt7lzhaTGIvd5--4sINJAMQSzk3w27P3scwxamPMM", "TemplateTask")

        val templateTaskValues = templateTaskValueRange.toValueList()

        val templates = templateValueRange.toValueList().filter { it.getOrStringZero(4) == categoryId }.map {
            val templateId = it.getOrStringZero(0)
            val tasks =
                templateTaskValues.filter { templateTaskValue -> templateTaskValue.getOrStringZero(4) == templateId }
                    .map { templateTaskValue ->
                        TemplateTask(
                            id = templateTaskValue.getOrStringZero(0),
                            name = templateTaskValue.getOrStringEmpty(1),
                            defaultDays = templateTaskValue.getOrStringEmpty(2).split(",")
                                .map { defaultDay -> defaultDay.toInt() },
                            dailyTimes = templateTaskValue.getOrStringZero(3).toInt(),
                        )
                    }
            Template(
                id = templateId,
                name = it.getOrStringEmpty(1),
                description = it.getOrStringEmpty(2),
                templateIconUrl = it.getOrNull(3).ifEmptyToNull(),
                categoryId = it.getOrStringZero(4),
                tasks = tasks
            )
        }

        return if (categoryId != null) {
            templates.filter { it.categoryId == categoryId }
        } else {
            templates
        }
    }
}

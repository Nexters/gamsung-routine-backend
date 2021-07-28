package com.gamsung.domain.routine

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class RoutineTask(
    @Id
    var id: String, // UUID
    var profileId: String,
    var title: String,
    var timesOfWeek: Int, // 주당 횟수
    var timesOfDay: Int, // 1일 횟수
    var notify: Boolean, // 알람 여부
    var days: List<Int>, // 월 수 금
    var times: List<String>, // 09:00, 10:00
    var category: String, // Category. 아마도 Enum?
    var templateId: String, // UUID
    var order: Int, // 나열 순서
)

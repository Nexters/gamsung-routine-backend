package com.gamsung.domain.routine

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class RoutineTask(
    @Id
    val id: String, // UUID

    val profileId: String,
    
    val title: String,
    
    val timesOfWeek: Long, // 주당 횟수
    
    val timesOfDay: Long, // 1일 횟수
    
    val notify: Boolean, // 알람 여부
    
    val dayOfWeek: List<String>?, // DayOfWeek
    
    val time: List<String>?, // 09:00
    
    val category: String, // Category. 아마도 Enum?
    
    val templateId: String?, // UUID
    
    val order: Long,
)
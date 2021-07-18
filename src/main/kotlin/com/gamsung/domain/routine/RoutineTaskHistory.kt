package com.gamsung.domain.routine

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class RoutineTaskHistory (

    @Id
    var id: Long,
    var profileId: Long,
    var taskId: Long,
    var completedAt: LocalDateTime
)

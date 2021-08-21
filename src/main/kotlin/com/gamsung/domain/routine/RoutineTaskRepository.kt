package com.gamsung.domain.routine

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RoutineTaskRepository: MongoRepository<RoutineTask, String> {
    fun findByProfileId(profileId: String): List<RoutineTask>
    fun findByTaskIdAndProfileId(taskId: String, profileId: String): Optional<RoutineTask>
}

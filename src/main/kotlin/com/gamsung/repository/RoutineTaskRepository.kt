package com.gamsung.repository

import com.gamsung.domain.routine.RoutineTask
import org.springframework.data.mongodb.repository.MongoRepository

interface RoutineTaskRepository: MongoRepository<RoutineTask, String> {
    fun findByProfileId(profileId: String): List<RoutineTask>
}

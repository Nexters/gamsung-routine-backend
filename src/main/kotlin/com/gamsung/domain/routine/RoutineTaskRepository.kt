package com.gamsung.domain.routine

import org.springframework.data.mongodb.repository.MongoRepository

interface RoutineTaskRepository: MongoRepository<RoutineTask, String> {
    fun findByProfileId(profileId: String): List<RoutineTask>
}

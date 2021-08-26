package com.gamsung.domain.routine

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RoutineTaskRepository: MongoRepository<RoutineTask, String> {
    fun findByProfileId(profileId: String): List<RoutineTask>
    fun findByCode(code: String): List<RoutineTask>
    fun findByCodeIn(codes: List<String>): List<RoutineTask>
    fun findByCodeAndProfileId(code: String, profileId: String): Optional<RoutineTask>
}

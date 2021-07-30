package com.gamsung.domain.routine

import com.gamsung.repository.RoutineTaskUnitRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class RoutineTaskUnitServiceTest {

    @Autowired
    lateinit var routineTaskUnitRepository: RoutineTaskUnitRepository

    @Test
    fun sample() {
        routineTaskUnitRepository.findAll()
    }
}
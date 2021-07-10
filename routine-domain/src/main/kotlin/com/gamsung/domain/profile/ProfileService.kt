package com.gamsung.domain.profile

import org.springframework.stereotype.Service
import java.util.*

@Service
interface ProfileService {
    fun get(): Profile
}

@Service
class SampleProfileService : ProfileService {
    override fun get(): Profile {
        return Profile(
            id = UUID.randomUUID().toString(),
            name = "Test",
            profileImageUrl = "http://test.com",
        )
    }
}

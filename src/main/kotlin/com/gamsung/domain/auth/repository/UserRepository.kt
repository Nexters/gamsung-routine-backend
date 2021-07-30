package com.gamsung.domain.auth.repository

import com.gamsung.domain.auth.SocialType
import com.gamsung.domain.auth.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByUsernameAndActive(id: String, active: Boolean = true): User?
    fun findByProviderIdAndSocialTypeAndActive(providerId: String, socialType: SocialType, active: Boolean = true): User?
}

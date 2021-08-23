package com.gamsung.domain.auth.repository

import com.gamsung.domain.auth.document.User
import com.gamsung.domain.auth.service.SocialType
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByUsernameAndActive(id: String, active: Boolean = true): User?
    fun findByIdIn(ids: List<String>): List<User>
    fun findByProviderIdAndSocialTypeAndActive(providerId: String, socialType: SocialType, active: Boolean = true): User?
}

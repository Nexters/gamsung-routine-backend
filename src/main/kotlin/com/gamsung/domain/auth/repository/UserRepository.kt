package com.gamsung.domain.auth.repository

import com.gamsung.domain.auth.SocialType
import com.gamsung.domain.auth.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

interface UserRepository: MongoRepository<User, String> {
    fun findByIdAndActive(id: String, active: Boolean = true): User?
    fun findByProviderIdAndSocialTypeAndActive(providerId: String, socialType: SocialType, active: Boolean = true): User?
    fun save(user: User): User
}

package com.gamsung.domain.auth.repository

import com.gamsung.domain.auth.SocialType
import com.gamsung.domain.auth.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

//interface UserRepository: MongoRepository<User, String> {
@Repository
class UserRepository {
    fun findByIdAndActive(id: String, active: Boolean = true): User? {
        return User(
            id = id,
            providerId = "test",
            socialType = SocialType.KAKAO,
            email = "test",
            username = "test",
            nickname = "test",
            profileImageUrl = "test",
            password = "\$2a\$10\$RuSQK/GsVhlgClOnoePL5.ZUkwkxOi4sqIdl4X4JdeRkCTVUwTMMe",
        )
    }
    fun findByProviderIdAndSocialTypeAndActive(providerId: String, socialType: SocialType, active: Boolean = true): User? {
//        return User(
//            id = "67ed237e-8a4b-4226-9ab0-2c7962bd8046",
//            providerId = providerId,
//            socialType = socialType,
//            email = "test",
//            username = "test",
//            nickname = "test",
//            profileImageUrl = "test",
//        )
        return null
    }
    fun save(user: User): User {
        return user.copy(id = "67ed237e-8a4b-4226-9ab0-2c7962bd8046")
    }
}

package com.gamsung.domain.auth.document

import com.gamsung.domain.auth.service.SocialType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class User internal constructor(
    @Id
    val id: String?,

    val username: String,
    val password: String,
    val socialType: SocialType,
    val providerId: String,
    var nickname: String,
    var email: String,
    var profileImageUrl: String?,
    var thumbnailImageUrl: String?,
    var pushToken: String?,
    var pushNotification: Boolean? = true,
    var lastAccessTime: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true,
) {
    fun update(
        nickname: String,
        email: String,
        profileImageUrl: String?,
        thumbnailImageUrl: String?,
        pushToken: String?,
    ): User {
        this.nickname = nickname
        this.email = email
        this.profileImageUrl = profileImageUrl
        this.thumbnailImageUrl = thumbnailImageUrl
        this.pushToken = pushToken
        this.lastAccessTime = LocalDateTime.now()
        return this
    }

    fun modifyPushNotification(): Boolean {
        pushNotification = pushNotification?.not() ?: false
        return pushNotification!!
    }

    companion object {
        fun create(
            username: String,
            password: String,
            socialType: SocialType,
            providerId: String,
            nickname: String,
            email: String,
            profileImageUrl: String?,
            thumbnailImageUrl: String?,
            pushToken: String?,
        ): User {
            return User(
                id = null,
                username = username,
                password = password,
                socialType = socialType,
                providerId = providerId,
                nickname = nickname,
                email = email,
                profileImageUrl = profileImageUrl,
                thumbnailImageUrl = thumbnailImageUrl,
                pushToken = pushToken,
                pushNotification = true,
            )
        }
    }
}

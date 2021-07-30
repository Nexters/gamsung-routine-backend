package com.gamsung.domain.auth

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import java.time.LocalDateTime

data class SocialSignInRequest(
    val socialType: SocialType,
    val accessToken: String,
    val refreshToken: String,
    val pushToken: String?,
)

data class SocialSignInResponse(
    val accessToken: String,
    val refreshToken: String,
)

@Document
data class User(
    @Id
    val id: String?,

    val username: String,
    val password: String,
    val socialType: SocialType,
    val providerId: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String?,
    val thumbnailImageUrl: String?,
    val pushToken: String?,
    val lastAccessTime: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true,
) {
    companion object {
        fun create(
            username: String,
            password: String,
            socialType: SocialType,
            providerId: String,
            nickname: String,
            email: String,
            profileImageUrl: String?,
            pushToken: String?,
            thumbnailImageUrl: String?,
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
            )
        }
    }
}

class CustomUserDetails(
    private val _id: String,
    private val _username: String,
    private val _socialType: SocialType,
    private val _nickname: String,
    private val _password: String,
    private val _email: String,
    private val _profileImageUrl: String?,
    private val _authorities: MutableList<out GrantedAuthority>,
) : UserDetails {
    val id: String get() = _id
    val socialType: SocialType get() = _socialType
    val nickname: String get() = _nickname
    val email: String get() = _email
    val profileImageUrl: String? get() = _profileImageUrl

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return _authorities
    }

    override fun getPassword(): String {
        return _password
    }

    override fun getUsername(): String {
        return _username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

enum class SocialType {
    KAKAO;

    companion object {
        fun of(value: String): SocialType {
            return requireNotNull(values().find { it.name.lowercase() == value }) {
                "존재하지 않는 SocialType 입니다. [${value}]"
            }
        }
    }
}

sealed class Oauth2Provider {
    object Kakao : Oauth2Provider() {
        override fun builder(): ClientRegistration.Builder {
            return builder("kakao", ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .scope("profile_nickname", "profile_image", "account_email")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .clientId("20cd0daf6af348e4c32f8d7f0c534247")
                .clientSecret("TODO")
                .userNameAttributeName("id")
                .clientName("Kakao")
        }
    }

    abstract fun builder(): ClientRegistration.Builder

    internal fun builder(registrationId: String, method: ClientAuthenticationMethod): ClientRegistration.Builder {
        return ClientRegistration.withRegistrationId(registrationId)
            .apply {
                clientAuthenticationMethod(method)
                authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                redirectUri(DEFAULT_LOGIN_REDIRECT_URL)
            }
    }

    companion object {
        private const val DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}"
    }
}

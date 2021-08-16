package com.gamsung.domain.auth.service

import com.gamsung.api.UnAuthorizedException
import com.gamsung.domain.auth.document.User
import com.gamsung.domain.auth.repository.UserRepository
import com.gamsung.domain.external.kakao.KakaoApiClient
import com.gamsung.domain.external.kakao.KakaoResponse
import com.gamsung.domain.security.AccountHolder
import com.gamsung.infra.auth.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val kakaoApiClient: KakaoApiClient,
    private val authenticationManager: AuthenticationManager,
    private val accessTokenProvider: JwtTokenProvider,
    private val refreshTokenProvider: JwtTokenProvider,
    private val userDetailsService: CustomUserDetailsService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val log: Logger = LoggerFactory.getLogger(AuthService::class.java)

    fun signIn(signInRequest: SocialSignInRequest): SocialSignInResponse {
        val userInfoResponse = try {
            kakaoApiClient.userInfo(accessToken = "Bearer ${signInRequest.accessToken}")
        } catch (e: Exception) {
            null
        }

        requireNotNull(userInfoResponse) { throw UnAuthorizedException("카카오 인증 실패") }
        require(userInfoResponse.statusCode == HttpStatus.OK) { throw UnAuthorizedException("카카오 인증 실패") }

        val userInfo = requireNotNull(userInfoResponse.body)

        val user = userRepository.findByProviderIdAndSocialTypeAndActive(
            providerId = userInfo.id.toString(),
            socialType = signInRequest.socialType
        )?.also { updateProfile(it, signInRequest.pushToken, userInfo) }
            ?: join(
                username = userInfo.toUsernameWithSocialType(signInRequest.socialType),
                providerId = userInfo.id.toString(),
                socialType = signInRequest.socialType,
                email = userInfo.properties.accountEmail ?: "",
                nickname = userInfo.properties.nickname,
                profileImage = userInfo.properties.profileImage,
                thumbnailImage = userInfo.properties.thumbnailImage,
                pushToken = signInRequest.pushToken,
            )

        val token = UsernamePasswordAuthenticationToken(user.username, user.providerId)
        val authenticate =
            authenticationManager.authenticate(token)
        val userDetails = userDetailsService.loadUserByUsername(user.username)

        SecurityContextHolder.getContext().authentication = authenticate

        return SocialSignInResponse(
            accessToken = accessTokenProvider.generateToken(userDetails),
            refreshToken = refreshTokenProvider.generateToken(userDetails),
        )
    }

    private fun updateProfile(user: User, pushToken: String?, userInfo: KakaoResponse): User {
        val updatedUser = user.update(
            nickname = userInfo.properties.nickname,
            email = userInfo.properties.accountEmail ?: "",
            profileImageUrl = userInfo.properties.profileImage,
            thumbnailImageUrl = userInfo.properties.thumbnailImage,
            pushToken = pushToken,
        )
        return userRepository.save(updatedUser)
    }

    private fun join(
        username: String,
        providerId: String,
        socialType: SocialType,
        email: String,
        nickname: String,
        profileImage: String?,
        thumbnailImage: String?,
        pushToken: String?,
    ): User {
        val user = User.create(
            username = username,
            password = passwordEncoder.encode(providerId),
            providerId = providerId,
            socialType = socialType,
            email = email,
            nickname = nickname,
            profileImageUrl = profileImage,
            thumbnailImageUrl = thumbnailImage,
            pushToken = pushToken,
        )

        return userRepository.save(user)
    }

    fun modifyPushNotification(): Boolean {
        val profile = AccountHolder.get()
        val user = requireNotNull(userRepository.findByIdOrNull(profile.id))
        val pushNotification = user.modifyPushNotification()
        userRepository.save(user)
        return pushNotification
    }
}

private fun KakaoResponse.toUsernameWithSocialType(socialType: SocialType): String {
    return "$socialType ${this.id}"
}

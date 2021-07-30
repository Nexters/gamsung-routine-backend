package com.gamsung.domain.auth.service

import com.gamsung.domain.auth.SocialSignInRequest
import com.gamsung.domain.auth.SocialSignInResponse
import com.gamsung.domain.auth.SocialType
import com.gamsung.domain.auth.User
import com.gamsung.domain.auth.repository.UserRepository
import com.gamsung.domain.external.kakao.KakaoApiClient
import com.gamsung.domain.external.kakao.KakaoResponse
import com.gamsung.infra.auth.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        val userInfoResponse = kakaoApiClient.userInfo(accessToken = "Bearer ${signInRequest.accessToken}")

        require(userInfoResponse.statusCode == HttpStatus.OK) { "인증 실패" }

        val userInfo = requireNotNull(userInfoResponse.body)

        val user = userRepository.findByProviderIdAndSocialTypeAndActive(
            providerId = userInfo.id.toString(),
            socialType = signInRequest.socialType
        )
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
}

private fun KakaoResponse.toUsernameWithSocialType(socialType: SocialType): String {
    return "$socialType ${this.id}"
}

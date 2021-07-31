package com.gamsung.api.dto

import com.gamsung.domain.auth.service.SocialSignInRequest
import com.gamsung.domain.auth.service.SocialType

data class SignInRequestDto(
    val accessToken: String,
    val refreshToken: String,
    val pushToken: String?,
)

data class SignInResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

fun SignInRequestDto.toRequestWith(socialType: SocialType) = SocialSignInRequest(
    socialType = socialType, accessToken = accessToken, refreshToken = refreshToken, pushToken = pushToken,
)
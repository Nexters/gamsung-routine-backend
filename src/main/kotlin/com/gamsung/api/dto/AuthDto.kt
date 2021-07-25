package com.gamsung.api.dto

import com.gamsung.domain.auth.SocialSignInRequest
import com.gamsung.domain.auth.SocialType

data class SignInRequestDto(
    val accessToken: String,
    val refreshToken: String,
)

data class SignInResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

fun SignInRequestDto.toRequestWith(socialType: SocialType) = SocialSignInRequest(
    socialType = socialType, accessToken = accessToken, refreshToken = refreshToken
)
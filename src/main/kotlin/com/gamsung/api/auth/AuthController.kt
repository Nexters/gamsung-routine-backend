package com.gamsung.api.auth

import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.dto.SignInRequestDto
import com.gamsung.api.dto.SignInResponseDto
import com.gamsung.api.dto.toRequestWith
import com.gamsung.domain.auth.service.AuthService
import com.gamsung.domain.auth.service.SocialType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/api/v1/auth/sign-in/{provider}")
    fun signIn(@PathVariable provider: String, @RequestBody dto: SignInRequestDto): ResponseDto<SignInResponseDto> {
        val socialType = SocialType.of(provider)
        val signInResult = authService.signIn(dto.toRequestWith(socialType))
        return ResponseDto.ok(
            SignInResponseDto(
                accessToken = signInResult.accessToken,
                refreshToken = signInResult.refreshToken,
            )
        )
    }
}

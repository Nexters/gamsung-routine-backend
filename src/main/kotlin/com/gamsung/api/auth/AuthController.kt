package com.gamsung.api.auth

import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.dto.SignInRequestDto
import com.gamsung.api.dto.SignInResponseDto
import com.gamsung.api.dto.toRequestWith
import com.gamsung.domain.auth.service.AuthService
import com.gamsung.domain.auth.service.SocialType
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


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

    @PatchMapping("/api/v1/auth/push-notification")
    fun modifyPushNotification(): ResponseDto<Boolean> {
        val isOn = authService.modifyPushNotification()
        return ResponseDto.ok(isOn)
    }

    val TEAM_ID = "787Q6972XA"
    val REDIRECT_URL = "https://app.bonkaemaster.com/apple/redirect"
    val CLIENT_ID = "com.routine.gamsung.app"
    val KEY_ID = "QV32V3QYL9"

    // 고정
    private val AUTH_URL = "https://appleid.apple.com";

    // 다운받은 AuthKey.p8 경로
    private val KEY_PATH = "static/AuthKey_QV32V3QYL9.p8";

    @RequestMapping(value = ["/login/getAppleAuthUrl"])
    @ResponseBody
    @Throws(Exception::class)
    fun getAppleAuthUrl(
        request: HttpServletRequest?
    ): String? {
        return (AUTH_URL + "/auth/authorize?client_id="
                + CLIENT_ID
                + "&redirect_uri="
                + REDIRECT_URL
                + "&response_type=code id_token&scope=name email&response_mode=form_post")
    }
}

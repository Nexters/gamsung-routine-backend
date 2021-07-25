package com.gamsung.domain.external.kakao

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@Service
@FeignClient(name = "kakaoApi", url = "https://kapi.kakao.com/")
interface KakaoApiClient {
    @GetMapping("/v2/user/me")
    fun userInfo(@RequestHeader("Authorization") accessToken: String): ResponseEntity<KakaoResponse>
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoResponse(
    val id: Long,
    val connectedAt: String,
    val properties: Properties,
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Properties(
        val nickname: String,
        val profileImage: String?,
        val thumbnailImage: String?,
        val accountEmail: String?,
    )
}

package com.gamsung.api.profile

import com.gamsung.api.dto.ResponseDto
import com.gamsung.api.profile.dto.ProfileDto
import com.gamsung.api.profile.dto.toDto
import com.gamsung.domain.profile.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/api/v1/profile")
    @ResponseStatus(HttpStatus.OK)
    fun get(): ResponseDto<ProfileDto> {
        val profile = profileService.get()

        return ResponseDto.ok(
            profile.toDto()
        )
    }
}

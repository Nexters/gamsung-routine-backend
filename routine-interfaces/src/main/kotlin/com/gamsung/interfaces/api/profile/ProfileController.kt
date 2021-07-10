package com.gamsung.interfaces.api.profile

import com.gamsung.domain.profile.ProfileService
import com.gamsung.interfaces.api.dto.ResponseDto
import com.gamsung.interfaces.api.profile.dto.ProfileDto
import com.gamsung.interfaces.api.profile.dto.toDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping("/api/v1/profile")
    fun get(): ResponseDto<ProfileDto> {
        val profile = profileService.get()

        return ResponseDto.ofSuccess(
            profile.toDto()
        )
    }
}

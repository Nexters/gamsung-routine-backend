package com.gamsung.interfaces.api.profile.dto

import com.gamsung.domain.profile.Profile

/**
 * @author Jongkook
 * @date : 2021/07/10
 */
data class ProfileDto(
    val id: String, // UUID
    val name: String,
    val profileImageUrl: String,
)

fun Profile.toDto() = ProfileDto(id = id, name = name, profileImageUrl = profileImageUrl)
package com.gamsung.api.profile.dto

import com.gamsung.domain.auth.service.Account

/**
 * @author Jongkook
 * @date : 2021/07/10
 */
data class ProfileDto(
    val id: String, // UUID
    val name: String,
    val profileImageUrl: String?,
    val thumbnailImageUrl: String?,
    val pushNotification: Boolean,
)

fun Account.toDto() =
    ProfileDto(id = id, name = nickname, profileImageUrl = profileImageUrl, thumbnailImageUrl = thumbnailImageUrl, pushNotification = pushNotification)
package com.gamsung.dto

/**
 * @author Jongkook
 * @date : 2021/07/10
 */

data class ProfileDto(
        var id: String, // UUID
        var name: String,
        var profileImageUrl: String,
) {
}
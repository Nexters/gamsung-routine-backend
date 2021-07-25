package com.gamsung.domain.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority

class KakaoOauth2User(
    private val id: String,
    private val properties: Properties
): OAuth2User {
    override fun getName(): String {
        return this.id
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to this.id,
            "name" to this.properties.nickname
        )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(OAuth2UserAuthority(attributes))
    }

    data class Properties(
        val nickname: String
    )
}
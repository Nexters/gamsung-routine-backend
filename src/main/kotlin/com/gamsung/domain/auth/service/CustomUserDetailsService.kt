package com.gamsung.domain.auth.service

import com.gamsung.api.UnAuthorizedException
import com.gamsung.domain.auth.document.User
import com.gamsung.domain.auth.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameAndActive(username) ?: throw UsernameNotFoundException("UsernameNotFound [$username]")

        return user.toUserDetails()
    }
}

private fun User.toUserDetails() = CustomUserDetails(
    _id = this.id!!,
    _username = this.username,
    _socialType = this.socialType,
    _nickname = this.nickname,
    _password = this.password,
    _email = this.email,
    _profileImageUrl = this.profileImageUrl,
    _thumbnailImageUrl = this.thumbnailImageUrl,
    _pushNotification = this.pushNotification ?: true,
    _authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
)

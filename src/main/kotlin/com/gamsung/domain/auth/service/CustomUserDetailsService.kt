package com.gamsung.domain.auth.service

import com.gamsung.domain.auth.CustomUserDetails
import com.gamsung.domain.auth.User
import com.gamsung.domain.auth.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        // TODO Exception 정의
        val user = userRepository.findByIdAndActive(id) ?: throw Exception("User Not Found Exception")

        return user.toUserDetails()
    }
}

private fun User.toUserDetails() = CustomUserDetails(
    _id = this.id!!,
    _socialType = this.socialType,
    _username = this.username,
    _nickname = this.nickname,
    _password = "\$2a\$10\$6YkckgcVT9SNkWaIUXXt1epTajAi0DSgsFUwH9yTUas4I1ckEH6fy",
    _email = this.email,
    _profileImageUrl = this.profileImageUrl,
    _authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
)

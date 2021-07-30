package com.gamsung.domain.security

import com.gamsung.domain.auth.service.Account
import com.gamsung.domain.auth.service.CustomUserDetails
import com.gamsung.domain.auth.service.toAccount
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object AccountHolder {
    fun get(): Account {
        return requireNotNull(getOrNull()) { "Account 가 설정되지 않음" }
    }

    fun getOrNull(): Account? {
        return try {
            SecurityContextHolder.getContext().authentication.getAccount()
        } catch (e: Exception) {
            null
        }
    }

    private fun Authentication.getAccount(): Account? = if (principal is CustomUserDetails) (principal as CustomUserDetails).toAccount() else null
}
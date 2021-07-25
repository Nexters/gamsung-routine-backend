package com.gamsung.configuration

import com.gamsung.domain.auth.Oauth2Provider
import com.gamsung.domain.auth.service.CustomUserDetailsService
import com.gamsung.domain.security.JwtRequestFilter
import com.gamsung.infra.auth.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository

@Configuration
class AuthConfig {
    @Bean
    fun accessTokenProvider(
        @Value("\${auth.jwt.access.secret}") secret: String,
        @Value("\${auth.jwt.access.expire-days}") expireDays: Long,
    ) = JwtTokenProvider(secret = secret, validateDays = expireDays)

    @Bean
    fun refreshTokenProvider(
        @Value("\${auth.jwt.refresh.secret}") secret: String,
        @Value("\${auth.jwt.refresh.expire-days}") expireDays: Long,
    ) = JwtTokenProvider(secret = secret, validateDays = expireDays)

    @Bean
    fun jwtRequestFilter(
        accessTokenProvider: JwtTokenProvider,
        userDetailsService: CustomUserDetailsService,
    ): JwtRequestFilter {
        return JwtRequestFilter(accessTokenProvider, userDetailsService)
    }

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        val kakao = Oauth2Provider.Kakao.builder().build()
        return InMemoryClientRegistrationRepository(listOf(kakao))
    }
}
package com.gamsung.domain.security

import com.gamsung.domain.auth.service.CustomUserDetailsService
import com.gamsung.infra.auth.JwtTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtRequestFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: CustomUserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = requireNotNull(request.authorizationHeader) {
            filterChain.doFilter(request, response)
            return
        }

        require(token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = token.substring(7)
        val username = try {
            jwtTokenProvider.usernameFromToken(jwtToken)
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            null
        }

        requireNotNull(username) {
            filterChain.doFilter(request, response)
            return
        }

        val userDetails = userDetailsService.loadUserByUsername(username)
        if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}

private val HttpServletRequest.authorizationHeader: String?
    get() = this.getHeader("Authorization")
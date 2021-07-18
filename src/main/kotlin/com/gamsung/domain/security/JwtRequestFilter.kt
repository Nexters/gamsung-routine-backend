package com.gamsung.domain.security

import com.gamsung.infra.auth.JwtTokenProvider
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtRequestFilter(
    private val jwtTokenProvider: JwtTokenProvider
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
        val userId = try {
            jwtTokenProvider.usernameFromToken(jwtToken)
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            null
        }

        requireNotNull(userId) {
            filterChain.doFilter(request, response)
            return
        }

        // TODO userService

        filterChain.doFilter(request, response)
    }
}

private val HttpServletRequest.authorizationHeader: String?
    get() = this.getHeader("Authorization")
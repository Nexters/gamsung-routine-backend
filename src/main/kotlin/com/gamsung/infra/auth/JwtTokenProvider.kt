package com.gamsung.infra.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class JwtTokenProvider(
    private val secret: String,
    private val tokenValidateHour: Long,
) {
    fun usernameFromToken(token: String): String = claimFrom(token, Claims::getSubject)

    fun expirationDateFrom(token: String): Date = claimFrom(token, Claims::getExpiration)

    fun generateToken(userDetails: UserDetails): String {
        val millis = tokenValidateHour * 60 * 60 * 1000
        return Jwts.builder().apply {
            setClaims(mutableMapOf<String, Any>())
            setSubject(userDetails.username)
            setIssuedAt(Date(System.currentTimeMillis()))
            setExpiration(Date(System.currentTimeMillis() * millis))
            signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
        }.compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = usernameFromToken(token)
        return username == userDetails.username && !tokenExpired(token)
    }

    private fun tokenExpired(token: String): Boolean {
        val expirationDateFrom = expirationDateFrom(token)
        return expirationDateFrom < Date()
    }

    private fun <T> claimFrom(token: String, resolver: (Claims) -> T): T {
        return resolver(this.allClaimsFrom(token))
    }

    private fun allClaimsFrom(token: String) =
        Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
}
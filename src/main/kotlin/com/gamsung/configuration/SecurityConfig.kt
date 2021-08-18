package com.gamsung.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.gamsung.api.dto.ResponseDto
import com.gamsung.domain.auth.service.CustomUserDetailsService
import com.gamsung.domain.security.JwtRequestFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter,
    private val userDetailsService: CustomUserDetailsService,
) : WebSecurityConfigurerAdapter() {
    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    fun configureAuthentication(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService(this.userDetailsService)
            .passwordEncoder(this.passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/oauth2/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/api/v1/auth/sign-in/**").permitAll()
            .anyRequest().permitAll()
//            .antMatchers("/api/**").authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .formLogin().disable()
            .exceptionHandling()
            .authenticationEntryPoint { request, response, _ ->
                log.warn("인증되지 않았습니다. [${request.requestURI}]")

                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.outputStream.use {
                    val responseDto = ResponseDto.error(status = HttpStatus.UNAUTHORIZED, message = "인증되지 않았습니다.")
                    OBJECT_MAPPER.writeValue(it, responseDto)
                    it.flush()
                }
            }
            .and()
            .oauth2Login()
            .userInfoEndpoint()

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers("/docs/**")
            .antMatchers("/.well-known/**")
            .antMatchers("/swagger-ui.html")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
        private val OBJECT_MAPPER = ObjectMapper()
    }
}

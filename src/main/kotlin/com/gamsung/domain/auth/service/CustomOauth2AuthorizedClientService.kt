package com.gamsung.domain.auth.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOauth2AuthorizedClientService: OAuth2AuthorizedClientService {
    private val log: Logger = LoggerFactory.getLogger(CustomOauth2AuthorizedClientService::class.java)

    override fun saveAuthorizedClient(authorizedClient: OAuth2AuthorizedClient, principal: Authentication) {
        val registrationId = authorizedClient.clientRegistration.registrationId
        val accessToken = authorizedClient.accessToken

        val oauth2User = principal.principal as OAuth2User
        val id = oauth2User.name
        val name = oauth2User.getAttribute<String>("nickname")

        log.info("attr : ${oauth2User.attributes}")
        log.info("id : $id name : $name")
    }

    override fun <T : OAuth2AuthorizedClient?> loadAuthorizedClient(
        clientRegistrationId: String?,
        principalName: String?
    ): T {
        throw NotImplementedError()
    }

    override fun removeAuthorizedClient(clientRegistrationId: String?, principalName: String?) {
        throw NotImplementedError()
    }
}
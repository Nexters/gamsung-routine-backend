package com.gamsung.domain.profile

import org.springframework.data.mongodb.core.mapping.Document

data class Profile(
    val id: String,
    val name: String,
    val profileImageUrl: String,
)

@Document
data class User(
    val id: String,
    val oAuth2Id: String,
    val email: String,
    val profileImageUrl: String,
)
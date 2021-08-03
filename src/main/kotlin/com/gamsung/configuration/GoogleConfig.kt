package com.gamsung.configuration

import com.gamsung.infra.google.GoogleSheetTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleConfig(
    @Value("\${google.app.email}")
    private val serviceAccountClientEmail: String,
    @Value("\${google.app.name}")
    private val appName: String,
) {
    @Bean
    fun googleSheetTemplate(): GoogleSheetTemplate {
        return GoogleSheetTemplate(serviceAccountClientEmail = serviceAccountClientEmail, appName = appName)
    }
}
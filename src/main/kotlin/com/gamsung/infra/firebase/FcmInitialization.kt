package com.gamsung.infra.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FcmInitialization {
    @Bean
    fun initFcm() = ApplicationRunner {
        val fcmCredential = FileInputStream("static/bonkaemaster-firebase-adminsdk-syfro-e1a7fe5eb4.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(fcmCredential))
            .build()
        FirebaseApp.initializeApp(options)
    }
}
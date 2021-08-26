package com.gamsung.domain.push

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class FirebaseCloudMessageService {

    fun send(token: String, title: String, body: String) {
        val message = Message.builder()
            .setToken(token)
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .build()
        FirebaseMessaging.getInstance().send(message)
    }

    fun sendMulticastMessage(token: MutableList<String>, title: String, body: String) {
        val message = MulticastMessage.builder()
            .addAllTokens(token)
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .build()
        FirebaseMessaging.getInstance().sendMulticast(message)
    }

}
package com.gamsung.infra.firebase

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

data class FcmMessage(
    var validateOnly:Boolean? = null,
    var message: Message? = null
)

data class FcmNotification(
    var notification: Notification? = null,
    var token: String? = null
)
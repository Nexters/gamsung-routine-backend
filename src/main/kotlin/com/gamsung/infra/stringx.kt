package com.gamsung.infra

fun String?.ifEmptyToNull(): String? {
    return if(this?.isEmpty() == true) {
        null
    } else {
        this
    }
}

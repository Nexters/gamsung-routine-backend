package com.gamsung.api.dto

import org.springframework.http.HttpStatus

data class ResponseDto<T> internal constructor(
    val status: Int,
    val message: String,
    val data: T? = null,
) {
    companion object {
        fun <T> ok(data: T) = ResponseDto(
            status = 200,
            message = "성공",
            data = data,
        )

        fun <T> ok(data: T, message: String) = ResponseDto(
            status = 200,
            message = message,
            data = data,
        )

        fun <T> error(message: String) = ResponseDto<T>(
            status = 5001,
            message = message,
            data = null
        )

        fun error(status: HttpStatus, message: String?): ResponseDto<Nothing> {
            return ResponseDto(
                status = status.value(),
                message = message ?: "",
            )
        }

        fun error(status: Int, message: String?): ResponseDto<Nothing> {
            return ResponseDto(
                status = status,
                message = message ?: "",
            )
        }
    }
}

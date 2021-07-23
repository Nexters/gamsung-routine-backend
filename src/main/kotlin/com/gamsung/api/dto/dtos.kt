package com.gamsung.api.dto

data class ResponseDto<T> internal constructor(
    val status: Int,
    val message: String,
    val data: T?,
) {
    companion object {
        fun <T> ok(data: T) = ResponseDto(
            status = 200,
            message = "성공",
            data = data,
        )
    }
}

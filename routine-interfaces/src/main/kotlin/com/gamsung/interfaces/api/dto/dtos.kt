package com.gamsung.interfaces.api.dto

data class ResponseDto<T> internal constructor(
    val code: Int,
    val message: String,
    val data: T?,
) {
    companion object {
        fun <T> ofSuccess(data: T) = ResponseDto(
            code = 200,
            message = "성공",
            data = data,
        )
    }
}

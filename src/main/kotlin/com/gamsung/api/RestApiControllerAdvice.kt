package com.gamsung.api

import com.gamsung.api.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(e: IllegalArgumentException): ResponseDto<Nothing> {
        log.warn(e.message)
        return ResponseDto.error(
            status = HttpStatus.BAD_REQUEST,
            message = e.message ?: ""
        )
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException::class)
    fun handleException(e: UnAuthorizedException): ResponseDto<Nothing> {
        log.warn(e.message)
        return ResponseDto.error(
            status = HttpStatus.UNAUTHORIZED,
            message = e.message ?: ""
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseDto<Nothing> {
        log.error(e.message, e)
        return ResponseDto.error(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = "알 수 없는 에러가 발생하였습니다."
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RestApiControllerAdvice::class.java)
    }
}
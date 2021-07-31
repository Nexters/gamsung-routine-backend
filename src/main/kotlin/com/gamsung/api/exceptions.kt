package com.gamsung.api

open class BusinessException: RuntimeException {
    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)
}

class UnAuthorizedException: BusinessException {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
}

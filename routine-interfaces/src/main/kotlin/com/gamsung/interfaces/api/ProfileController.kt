package com.gamsung.interfaces.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController {
    @GetMapping("/api/v1/profile")
    fun get(): String {
        return "test"
    }
}

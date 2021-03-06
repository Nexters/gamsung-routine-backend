package com.gamsung.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/static/docs/")
        registry.addResourceHandler("/.well-known/**").addResourceLocations("classpath:/static/.well-known/")
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/")
    }
}

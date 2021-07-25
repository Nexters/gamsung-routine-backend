package com.gamsung.configuration

import com.gamsung.domain.external.External
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [External::class])
class FeignConfig

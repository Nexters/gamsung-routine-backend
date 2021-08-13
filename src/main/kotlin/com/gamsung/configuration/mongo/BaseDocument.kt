package com.gamsung.configuration.mongo

import com.gamsung.infra.lateInit
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class BaseDocument {
    @Id
    var id: String? = lateInit()

    @CreatedDate
    var createdDateTime: LocalDateTime = lateInit()

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime = lateInit()
}

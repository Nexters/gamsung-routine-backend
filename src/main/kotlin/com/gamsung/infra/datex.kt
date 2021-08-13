package com.gamsung.infra

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toDateString(): String = this.format(DateTimeFormatter.BASIC_ISO_DATE)
fun LocalDate.toDateString(): String = this.format(DateTimeFormatter.BASIC_ISO_DATE)

package com.gamsung

import java.time.LocalDate

fun generateDate(currDate: LocalDate) : String {
    val monthString = currDate.month.value.toString()
    val month = if (monthString.length < 2) ("0$monthString") else monthString

    val dayString = currDate.dayOfMonth.toString()
    val day = if (dayString.length < 2) ("0$dayString") else dayString

    return currDate.year.toString() + month + day
}
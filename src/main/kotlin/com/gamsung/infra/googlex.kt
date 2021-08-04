package com.gamsung.infra

import com.google.api.services.sheets.v4.model.ValueRange

@Suppress("UNCHECKED_CAST")
fun ValueRange?.toValueList(): List<List<String>> =
    if (this?.values == null) {
        emptyList()
    } else {
        this.getValues().toList().filterIndexed { index, _ -> index != 0 } as List<List<String>>
    }

fun List<String>.getOrStringZero(index: Int) = this.getOrElse(index) { "0" }
fun List<String>.getOrStringEmpty(index: Int) = this.getOrElse(index) { "" }
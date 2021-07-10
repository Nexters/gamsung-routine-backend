package com.gamsung.dto

/**
 * @author Jongkook
 * @date : 2021/07/10
 * .
 * done 여부는 history에서 체크
 */
data class RoutineTaskDto(
    var id: String, // UUID
    var title: String,
    var timesOfWeek: Long, // 주당 횟수
    var timesOfDay: Long, // 1일 횟수
    var notify: Boolean, // 알람 여부
    var dayOfWeek: List<String>?, // DayOfWeek
    var time: List<String>?, // 09:00
    var category: String, // Category. 아마도 Enum?
    var templateId: String?, // UUID
    var order: Long,
) {
    //
}
package com.gamsung.domain.unit

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document
data class RoutineTaskUnit(

    @Id
    val id: String?, // "20210702:profileId:taskId", key가 없을 시 최초 생성
    val profileId: String,
    var date: String, // 월, 일이 한자리수일 때, '0'붙일 것
    var localDate: LocalDate,
    val taskId: String,
    val title: String, // Task Title

    // ** 아래 필드는, 루틴 수정시 해당 "주"에 있는 history에 반영 **
    val days: List<Int>?, // 월 수 금, 월요일이 '1'
    val times: List<String>?, // [09:00, 10:00]

    val friendIds: List<String>?, // 친구 관련 데이터 필요시 id를 바탕으로 query 후에 채워준다

    // 완료 될때마다 업데이트 되야 하는 필드들
    val completeCount: Int,
    val completedDateList: MutableList<LocalDateTime> //[“2020-08-05:12:02:05”, “2020-08-05:12:02:05”, “2020-08-05:12:02:05”]
) {

    fun update(
        date: String,
        localDate: LocalDate
    ): RoutineTaskUnit {
        this.date = date
        this.localDate = localDate
        return this
    }

    fun complete(localDateTime: LocalDateTime): RoutineTaskUnit {
        this.completedDateList.add(localDateTime)
        return this
    }

    companion object {
        fun create(
            id: String,
            profileId: String,
            date: String,
            localDate: LocalDate,
            taskId: String,
            title: String,
            days: List<Int>?,
            times: List<String>?,
            friendIds: List<String>?,
            completeCount: Int
        ): RoutineTaskUnit {
            return RoutineTaskUnit(
                id = id,
                profileId = profileId,
                date = date,
                localDate = localDate,
                taskId = taskId,
                title = title,
                days = days,
                times = times,
                friendIds = friendIds,
                completeCount = completeCount,
                completedDateList = mutableListOf()
            )
        }
    }
}

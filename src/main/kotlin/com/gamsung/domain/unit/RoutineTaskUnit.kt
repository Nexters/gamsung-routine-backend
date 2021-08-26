package com.gamsung.domain.unit

import com.gamsung.configuration.mongo.BaseDocument
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document
data class RoutineTaskUnit(
    val unitId: String, // "20210702:profileId:taskId", key가 없을 시 최초 생성
    val profileId: String,
    var date: String, // 월, 일이 한자리수일 때, '0'붙일 것
    var localDate: LocalDate,
    val taskId: String,
    val taskCode: String,
    var title: String, // Task Title

    // ** 아래 필드는, 루틴 수정시 해당 "주"에 있는 history에 반영 **
    var days: List<Int>?, // 월 수 금, 월요일이 '1'
    var times: List<String>?, // [09:00, 10:00]

    val friendIds: List<String>?, // 친구 관련 데이터 필요시 id를 바탕으로 query 후에 채워준다
    var isDelayUnit: Boolean = false,

    // 완료 될때마다 업데이트 되야 하는 필드들
    val completedDateList: MutableList<LocalDateTime>, //[“2020-08-05:12:02:05”, “2020-08-05:12:02:05”, “2020-08-05:12:02:05”]
    var delayedDateTime: LocalDateTime?

) : BaseDocument() {
    val completeCount: Int
        @Transient
        get() = completedDateList.size

    val timesOfDay: Int
        @Transient
        get() = times?.size ?: -1

    fun delay(
        date: String,
        localDate: LocalDate
    ): RoutineTaskUnit {
        this.date = date
        this.localDate = localDate
//        this.isDelayUnit = true
        return this
    }

    fun back(): RoutineTaskUnit {
        this.completedDateList.removeAt(this.completedDateList.size - 1)
        return this
    }

    fun complete(localDateTime: LocalDateTime): RoutineTaskUnit {
        this.completedDateList.add(localDateTime)
        return this
    }

    companion object {
        fun create(
            unitId: String,
            profileId: String,
            date: String,
            localDate: LocalDate,
            taskId: String,
            taskCode: String,
            title: String,
            days: List<Int>?,
            times: List<String>?,
            friendIds: List<String>?,

            ): RoutineTaskUnit {
            return RoutineTaskUnit(
                unitId = unitId,
                profileId = profileId,
                date = date,
                localDate = localDate,
                taskId = taskId,
                taskCode = taskCode,
                title = title,
                days = days,
                times = times,
                friendIds = friendIds,
                completedDateList = mutableListOf(),
                delayedDateTime = null
            )
        }
    }
}

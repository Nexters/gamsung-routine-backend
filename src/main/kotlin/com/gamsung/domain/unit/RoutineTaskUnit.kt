package com.gamsung.domain.unit

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Document
data class RoutineTaskUnit(

    @Id
    val id: String?, // "20210702:profileId:taskId", key가 없을 시 최초 생성
    val profileId: String,
    val date: String, // 월, 일이 한자리수일 때, '0'붙일 것
    val localDate: LocalDate,
    val taskId: String,
    val title: String, // Task Title

    // ** 이 4 필드는, 루틴 수정시 해당 "주"에 있는 history에 반영 **
    val timesOfDay: Int, // 하루면 총 몇회해야 하나
    val days: List<Int>?, // 월 수 금, 월요일이 '1'
    val times: List<String>?, // [09:00, 10:00]

    val friendIds: List<String>?, // 친구 관련 데이터 필요시 id를 바탕으로 query 후에 채워준다
//    Friends: [
//      {
//          Id, name, profileImage, completeCount
//      }
//    ]

    // 완료 될때마다 업데이트 되야 하는 필드들
    val completeCount: Int,
//    val completedDateList: List<LocalDate> //[“2020-08-05:12:02:05”, “2020-08-05:12:02:05”, “2020-08-05:12:02:05”]
) {
//    companion object {
//        fun create(
//
//        ): RoutineTaskUnit {
//            return RoutineTaskUnit(
//
//            )
//        }
//    }
}

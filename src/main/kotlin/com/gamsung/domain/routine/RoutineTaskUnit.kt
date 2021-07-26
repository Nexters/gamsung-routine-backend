package com.gamsung.domain.routine

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class RoutineTaskUnit(

    @Id
    val id: String, // "20210702:profileId:taskId", key가 없을 시 최초 생성
    val profileId: String,
//    val dateString: String, //20210702
    val taskId: String,
    val title: String,

    // ** 이 4 필드는, 루틴 수정시 해당 "주"에 있는 history에 반영 **
    val timesOfWeek: Int,
    val timesOfDay: Int,
    val days: List<Int>?, // 월 수 금
    val times: List<String>?, // [09:00, 10:00]
    val targetCount: Int,

    val friendIds: List<String>?, // 친구 관련 데이터 필요시 id를 바탕으로 query 후에 채워준다
//    Friends: [
//      {
//          Id, name, profileImage, completeCount
//      }
//    ]

    //get monthly routimes
//    val year: String, //2021
//    val month: String, //8
//    val day: String, //1
    // better performance
    val date: String, //20210801

    // 완료 될때마다 업데이트 되야 하는 필드들
    val completeCount: Int,
    val completedAt: List<LocalDate> //[“2020-08-05:12:02:05”, “2020-08-05:12:02:05”, “2020-08-05:12:02:05”]
)

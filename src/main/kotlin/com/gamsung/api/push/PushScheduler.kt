package com.gamsung.api.push

import com.gamsung.domain.auth.service.AuthService
import com.gamsung.domain.push.FirebaseCloudMessageService
import com.gamsung.domain.routine.RoutineTaskService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class PushScheduler(
    private val routineTaskService: RoutineTaskService,
    private val authService: AuthService,
    private val firebaseCloudMessageService: FirebaseCloudMessageService,
) {
    @Scheduled(cron = "0 */1 * * * ?")
    fun sendPushMessage() {
        val today = LocalDateTime.now()
        val todayDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val tasks = routineTaskService.getTodayNotifiedTasks(today)
        val todayTime = today.toLocalTime().minusMinutes(10).format(DateTimeFormatter.ofPattern("HH:mm"))
        val targetTasks = tasks.filter { it.times.any { time -> time == todayTime } }
        val profileIds = targetTasks.map { it.profileId }.distinct()
        val profiles = authService.getProfiles(profileIds)
            .filter { it.pushNotification ?: false }
            .associateBy { it.id!! }

        targetTasks.forEach {
            val profile = profiles[it.profileId]
            if (profile?.pushToken != null) {
                val friends = routineTaskService.getFriendsList(todayDate, it.code ?: "")
                var bodyMessage: String? = null
                when (friends?.size) {
                    0 -> throw Exception("")
                    1 -> bodyMessage = "테스크를 해결하고 몬스터를 없애주시게!"
                    2 -> {
                        for (user in friends) {
                            if (it.profileId != user.id) {
                                bodyMessage = "${user.nickname}과 함께 테스크를 해결하고 몬스터를 없애주시게!"
                                break
                            }
                        }
                    }
                    else -> {
                        if (friends != null) {
                            for (user in friends) {
                                if (it.profileId != user.id) {
                                    bodyMessage = "${user.nickname} 외 ${friends.size - 2}명과 함께 테스크를 해결하고 몬스터를 없애주시게!"
                                    break
                                }
                            }
                        }
                    }
                }
                firebaseCloudMessageService.send(
                    profile.pushToken!!,
                    it.title + " 10분 전!",
                    bodyMessage ?: "테스크를 해결하고 몬스터를 없애주시게!"
                )
                log.info("Push Send : $it")
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PushScheduler::class.java)
    }
}

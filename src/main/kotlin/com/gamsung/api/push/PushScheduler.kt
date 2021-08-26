package com.gamsung.api.push

import com.gamsung.api.routine.RoutineTaskController
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
        val tasks = routineTaskService.getTodayNotifiedTasks(today)

        val todayTime = today.toLocalTime().minusMinutes(10).format(DateTimeFormatter.ofPattern("HH:mm"))
        val targetTasks = tasks.filter { it.times.any { time -> time == todayTime } }
        val profileIds = targetTasks.map { it.profileId }.distinct()

        val profiles = authService.getProfiles(profileIds).filter { it.pushNotification ?: false }.associateBy { it.id!! }

        targetTasks.forEach {
            val profile = profiles[it.profileId]
            if (profile?.pushToken != null) {
                firebaseCloudMessageService.send(profile.pushToken!!, "뭘 보내?", "내용을 알려줘")
                log.info("Push Send : $it")
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PushScheduler::class.java)
    }
}

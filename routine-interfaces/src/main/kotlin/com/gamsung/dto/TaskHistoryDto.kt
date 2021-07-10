package com.gamsung.dto

import java.time.LocalDateTime

/**
 * @author Jongkook
 * @date : 2021/07/10
 */

data class TaskHistoryDto(
        var id: Long,
        var taskId: Long,
        var completedAt: LocalDateTime?
) {
    //
}
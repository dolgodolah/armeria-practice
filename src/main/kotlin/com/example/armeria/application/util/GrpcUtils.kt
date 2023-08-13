package com.example.armeria.application.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object GrpcUtils {

    fun toMilliseconds(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun toLocalDateTime(milliSeconds: Long): LocalDateTime {
        return Instant.ofEpochMilli(milliSeconds).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}
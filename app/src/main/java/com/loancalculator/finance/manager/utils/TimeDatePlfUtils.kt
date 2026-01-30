package com.loancalculator.finance.manager.utils

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs

object TimeDatePlfUtils {
    fun getCurrentTimeInZone(ianaZone: String): String {
        return try {
            val zoneId = ZoneId.of(ianaZone)  // 自动处理夏令时、规则变化
            val nowInZone = ZonedDateTime.now(zoneId)
            // 自定义格式（可根据需要调整）
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            nowInZone.format(formatter)
        } catch (e: Exception) {
            "Invalid time zone: $ianaZone (${e.message})"
            ""
        }
    }

    fun getCurrentTimeInZoneOffset(
        ianaZone: String,
        onlyTime: Boolean = true
    ): Pair<String, String> {
        return try {
            val zoneId = ZoneId.of(ianaZone)  // 自动处理夏令时、规则变化
            val nowInZone = ZonedDateTime.now(zoneId)
            // 自定义格式（可根据需要调整）
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            val formatter = if (onlyTime) {
                DateTimeFormatter.ofPattern("HH:mm:ss")
            } else {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            }

            Pair(nowInZone.format(formatter), getUtcOffsetString(nowInZone.offset))
        } catch (e: Exception) {
            "Invalid time zone: $ianaZone (${e.message})"
            Pair("", "")
        }
    }

    private fun getUtcOffsetString(zoneOffset: ZoneOffset): String {
        val hours = zoneOffset.totalSeconds / 3600
        val minutes = (zoneOffset.totalSeconds % 3600) / 60

        val sign = if (hours >= 0) "+" else "-"
        val absHours = abs(hours)

        return if (minutes == 0) {
            "UTC$sign%02d:00".format(absHours)
        } else {
            "UTC$sign%02d:%02d".format(absHours, Math.abs(minutes))
        }
    }

    fun getFileDatePlf(long: Long): String {
        val sim = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return sim.format(Date(long))
    }
}
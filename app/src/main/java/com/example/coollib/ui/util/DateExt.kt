package com.example.coollib.ui.util

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Instant.toReadableDate(
    pattern: String = "MMM dd, yyyy",
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        .withZone(zoneId)
    return formatter.format(this)
}

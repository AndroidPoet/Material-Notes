package com.androidpoet.materialnotes.designsystem

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val MONTHS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)

/** Formats "dd-MMM-yy hh:mm aa", e.g. "11-Jun-26 09:05 PM" — matches the original SimpleDateFormat. */
@OptIn(ExperimentalTime::class)
fun currentDateString(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.day.toString().padStart(2, '0')
    val month = MONTHS[now.month.ordinal]
    val year = (now.year % 100).toString().padStart(2, '0')
    val hour24 = now.hour
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val hour = hour12.toString().padStart(2, '0')
    val minute = now.minute.toString().padStart(2, '0')
    val amPm = if (hour24 < 12) "AM" else "PM"
    return "$day-$month-$year $hour:$minute $amPm"
}

/** Epoch millis now — stored on a note for stable, sortable, cross-device newest-first ordering. */
@OptIn(ExperimentalTime::class)
fun currentEpochMillis(): Long = Clock.System.now().toEpochMilliseconds()

package com.lczarny.lsnplanner.utils

import android.content.Context
import android.text.format.DateFormat
import com.lczarny.lsnplanner.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle

val currentTimestamp = { System.currentTimeMillis() }

val currentTimestampWithTime = { hour: Int, minute: Int ->
    LocalDateTime.now().atZone(ZoneId.systemDefault()).withHour(hour).withMinute(minute).toInstant().toEpochMilli()
}

val dateTimeFromEpochMilis = { milis: Long -> Instant.ofEpochMilli(milis).atZone(ZoneId.systemDefault()).toLocalDateTime() }

val dateFromEpochMilis = { milis: Long -> dateTimeFromEpochMilis(milis).toLocalDate() }

fun LocalDateTime.getTimestamp(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDate.getTimestamp(): Long = this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDateTime.getDayOfWeekNum(): Int = this.dayOfWeek.value

fun Month.getMonthName(context: Context): String = this.getDisplayName(TextStyle.FULL, context.resources.configuration.locales.get(0))

fun Int.toDayOfWeekString(context: Context): String = when (this) {
    1 -> context.resources.getString(R.string.monday)
    2 -> context.resources.getString(R.string.tuesday)
    3 -> context.resources.getString(R.string.wednesday)
    4 -> context.resources.getString(R.string.thursday)
    5 -> context.resources.getString(R.string.friday)
    6 -> context.resources.getString(R.string.saturday)
    7 -> context.resources.getString(R.string.sunday)
    else -> context.resources.getString(R.string.monday)
}

fun isDurationOverMidnight(duration: Int, startHour: Int, startMinute: Int): Boolean =
    ((startHour.toFloat() + (startMinute / 60)) + (duration.toFloat() / 60)) > 24.0f

infix fun LocalDate.isSameDate(date: LocalDate): Boolean =
    this.dayOfMonth == date.dayOfMonth && this.monthValue == date.monthValue && this.year == date.year

fun LocalDate.isBetweenDates(start: LocalDate, end: LocalDate): Boolean =
    this.isAfter(start.minusDays(1)) && this.isBefore(end.plusDays(1))

fun Long.convertMillisToSystemDateTime(context: Context): String =
    "${this.convertMillisToSystemDate(context)} ${this.convertMillisToSystemTime(context)}"

fun Long.convertMillisToSystemTime(context: Context): String = ((DateFormat.getTimeFormat(context)) as SimpleDateFormat).format(this)

fun Long.convertMillisToSystemDate(context: Context): String = ((DateFormat.getDateFormat(context)) as SimpleDateFormat).format(this)

fun formatTime(context: Context, hour: Int?, minute: Int?): String {
    val time = ((DateFormat.getTimeFormat(context)) as SimpleDateFormat).format(currentTimestampWithTime(hour ?: 0, minute ?: 0))

    if (time.indexOf(":") == 1) {
        return "0${time}"
    }

    return time
}

fun formatDuration(context: Context, hour: Int?, minute: Int?, duration: Int = 0): String {
    var endHour = hour?.plus((duration / 60))
    var endMinute = minute?.plus((duration % 60))

    if (endMinute != null && endMinute >= 60) {
        endMinute = endMinute % 60
        endHour?.plus(1)
    }

    return "${formatTime(context, hour, minute)} - ${formatTime(context, endHour, endMinute)}"
}

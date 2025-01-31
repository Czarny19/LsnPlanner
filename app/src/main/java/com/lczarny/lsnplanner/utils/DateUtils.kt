package com.lczarny.lsnplanner.utils

import android.content.Context
import android.text.format.DateFormat
import com.lczarny.lsnplanner.R
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Calendar

fun Calendar.getDayOfWeekNum(): Int =
    this.toInstant().toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek.value

fun Calendar.getMonthName(context: Context): String =
    Month.of(this.get(Calendar.MONTH) + 1).getDisplayName(TextStyle.FULL, context.resources.configuration.locales.get(0))

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

fun Int.mapAppDayOfWeekToCalendarDayOfWeek() = when (this) {
    1 -> 2
    2 -> 3
    3 -> 4
    4 -> 5
    5 -> 6
    6 -> 0
    7 -> 1
    else -> 1
}

fun Calendar.isSameDate(date: Calendar): Boolean =
    get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
            get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
            get(Calendar.YEAR) == date.get(Calendar.YEAR)

fun Long.convertMillisToSystemDateTime(context: Context): String {
    val date = ((DateFormat.getDateFormat(context)) as SimpleDateFormat).format(this)
    val time = ((DateFormat.getTimeFormat(context)) as SimpleDateFormat).format(this)
    return "$date $time"
}

fun formatTime(context: Context, hour: Int?, minute: Int?): String {
    val time = ((DateFormat.getTimeFormat(context)) as SimpleDateFormat).format(Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour ?: 0)
        set(Calendar.MINUTE, minute ?: 0)
    }.timeInMillis)

    if (time.indexOf(":") == 1) {
        return "0${time}"
    }

    return time
}


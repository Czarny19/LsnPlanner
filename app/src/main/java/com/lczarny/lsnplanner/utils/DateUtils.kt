package com.lczarny.lsnplanner.utils

import android.content.Context
import android.text.format.DateFormat
import com.lczarny.lsnplanner.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Calendar

fun getDayOfWeekDisplayValue(context: Context): String {
    val dayOfWeek = Calendar.getInstance().toInstant().toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek

    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> context.resources.getString(R.string.monday)
        DayOfWeek.TUESDAY -> context.resources.getString(R.string.tuesday)
        DayOfWeek.WEDNESDAY -> context.resources.getString(R.string.wednesday)
        DayOfWeek.THURSDAY -> context.resources.getString(R.string.thursday)
        DayOfWeek.FRIDAY -> context.resources.getString(R.string.friday)
        DayOfWeek.SATURDAY -> context.resources.getString(R.string.saturday)
        DayOfWeek.SUNDAY -> context.resources.getString(R.string.sunday)
    }
}

fun Long.convertMillisToSystemDateTime(context: Context): String {
    val date = ((DateFormat.getDateFormat(context)) as SimpleDateFormat).format(this)
    val time = ((DateFormat.getTimeFormat(context)) as SimpleDateFormat).format(this)
    return "$date $time"
}

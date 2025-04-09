package com.lczarny.lsnplanner.presentation.model.mapper

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.vector.ImageVector
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.data.common.model.ClassType
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate
import com.lczarny.lsnplanner.utils.formatTime
import com.lczarny.lsnplanner.utils.toDayOfWeekString

fun ClassType.toLabel(context: Context): String = when (this) {
    ClassType.Class -> context.getString(R.string.class_type_class)
    ClassType.Extracurricular -> context.getString(R.string.class_type_extracurricular)
    ClassType.Lecture -> context.getString(R.string.class_type_lecture)
    ClassType.Practical -> context.getString(R.string.class_type_practical)
    ClassType.Laboratory -> context.getString(R.string.class_type_laboratory)
    ClassType.Seminar -> context.getString(R.string.class_type_seminar)
    ClassType.Workshop -> context.getString(R.string.class_type_workshop)
    ClassType.Other -> context.getString(R.string.class_type_other)
}

fun ClassType.toPlanClassTypeIcon(): ImageVector = when (this) {
    ClassType.Class -> Icons.Filled.Class
    ClassType.Extracurricular -> Icons.Filled.PlusOne
    ClassType.Lecture -> Icons.AutoMirrored.Filled.LibraryBooks
    ClassType.Practical -> Icons.Filled.DesignServices
    ClassType.Laboratory -> Icons.Filled.Science
    ClassType.Seminar -> Icons.Filled.School
    ClassType.Workshop -> Icons.Filled.Handyman
    ClassType.Other -> Icons.Filled.Schedule
}

fun ClassScheduleType.toLabel(context: Context): String = when (this) {
    ClassScheduleType.Weekly -> context.getString(R.string.class_time_type_weekly)
    ClassScheduleType.Period -> context.getString(R.string.class_time_type_period)
    ClassScheduleType.Single -> context.getString(R.string.class_time_type_single)
}

fun ClassScheduleModel.timePreview(context: Context): String {
    val startsAt = formatTime(context, startHour, startMinute)
    val duration = "($durationMinutes ${context.getString(R.string.minutes_short)})"

    return when (this.type) {
        ClassScheduleType.Weekly -> "${weekDay!!.toDayOfWeekString(context)} $startsAt $duration"
        ClassScheduleType.Period -> "${weekDay!!.toDayOfWeekString(context)}* $startsAt $duration"
        ClassScheduleType.Single -> "${startDate?.convertMillisToSystemDate(context) ?: ""} $startsAt $duration".trim()
    }
}
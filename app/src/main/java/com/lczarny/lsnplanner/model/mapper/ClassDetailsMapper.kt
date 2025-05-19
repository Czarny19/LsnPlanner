package com.lczarny.lsnplanner.model.mapper

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.database.model.ClassSchedule
import com.lczarny.lsnplanner.database.model.ClassScheduleType
import com.lczarny.lsnplanner.database.model.ClassType
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate
import com.lczarny.lsnplanner.utils.formatTime
import com.lczarny.lsnplanner.utils.toDayOfWeekString

enum class ClassViewType(val raw: String) {
    List("list"),
    Timeline("timeline");

    companion object {
        fun from(find: String): ClassViewType = ClassViewType.entries.find { it.raw == find } ?: List
    }
}

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
    ClassType.Class -> AppIcons.CLASS_TYPE_CLASS
    ClassType.Extracurricular -> AppIcons.CLASS_TYPE_EXTRA
    ClassType.Lecture -> AppIcons.CLASS_TYPE_LECTURE
    ClassType.Practical -> AppIcons.CLASS_TYPE_PRACTICAL
    ClassType.Laboratory -> AppIcons.CLASS_TYPE_LAB
    ClassType.Seminar -> AppIcons.CLASS_TYPE_SEMINAR
    ClassType.Workshop -> AppIcons.CLASS_TYPE_WORKSHOP
    ClassType.Other -> AppIcons.CLASS_TYPE_OTHER
}

fun ClassScheduleType.toLabel(context: Context): String = when (this) {
    ClassScheduleType.Weekly -> context.getString(R.string.class_time_type_weekly)
    ClassScheduleType.Period -> context.getString(R.string.class_time_type_period)
    ClassScheduleType.Single -> context.getString(R.string.class_time_type_single)
}

fun ClassSchedule.timePreview(context: Context): String {
    val startsAt = formatTime(context, startHour, startMinute)
    val duration = "($durationMinutes ${context.getString(R.string.minutes_short)})"

    return when (this.type) {
        ClassScheduleType.Weekly -> "${weekDay!!.toDayOfWeekString(context)} $startsAt $duration"
        ClassScheduleType.Period -> "${weekDay!!.toDayOfWeekString(context)}* $startsAt $duration"
        ClassScheduleType.Single -> "${startDate?.convertMillisToSystemDate(context) ?: ""} $startsAt $duration".trim()
    }
}
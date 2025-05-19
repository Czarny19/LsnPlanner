package com.lczarny.lsnplanner.model.mapper

import android.content.Context
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.database.model.LessonPlanType

fun LessonPlanType.getLabel(context: Context) = when (this) {
    LessonPlanType.School -> context.getString(R.string.plan_type_school)
    LessonPlanType.University -> context.getString(R.string.plan_type_university)
}
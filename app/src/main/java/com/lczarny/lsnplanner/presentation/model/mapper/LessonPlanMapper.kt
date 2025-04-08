package com.lczarny.lsnplanner.presentation.model.mapper

import android.content.Context
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.LessonPlanType

fun LessonPlanType.getLabel(context: Context) = when (this) {
    LessonPlanType.School -> context.getString(R.string.plan_type_school)
    LessonPlanType.University -> context.getString(R.string.plan_type_university)
}
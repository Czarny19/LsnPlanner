package com.lczarny.lsnplanner.presentation.ui.lessonplan.model

import android.content.Context
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.LessonPlanType

fun toLessonPlanTypeLabelMap(context: Context): Map<LessonPlanType, String> = mapOf(
    Pair(LessonPlanType.School, context.getString(R.string.plan_type_school)),
    Pair(LessonPlanType.University, context.getString(R.string.plan_type_uni)),
)
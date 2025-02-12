package com.lczarny.lsnplanner.presentation.model

import android.content.Context
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.GradingSystem
import com.lczarny.lsnplanner.data.local.model.LessonPlanType

fun LessonPlanType.getLabel(context: Context) = when (this) {
    LessonPlanType.School -> context.getString(R.string.plan_type_school)
    LessonPlanType.University -> context.getString(R.string.plan_type_university)
}

fun GradingSystem.getLabel(context: Context) = when (this) {
    GradingSystem.AToFWithPlusMinus -> context.getString(R.string.plan_grading_system_a_to_f_with_plus_minus)
    GradingSystem.NumericalFrom2To5WithPlusMinus -> context.getString(R.string.plan_grading_system_2_to_5_with_plus_minus)
    GradingSystem.NumericalFrom2To5WithHalves -> context.getString(R.string.plan_grading_system_2_to_5_with_halves)
}

fun GradingSystem.getDescription(context: Context) = when (this) {
    GradingSystem.AToFWithPlusMinus -> context.getString(R.string.plan_grading_system_a_to_f_with_plus_minus_desc)
    GradingSystem.NumericalFrom2To5WithPlusMinus -> context.getString(R.string.plan_grading_system_2_to_5_with_plus_minus_desc)
    GradingSystem.NumericalFrom2To5WithHalves -> context.getString(R.string.plan_grading_system_2_to_5_with_halves_desc)
}
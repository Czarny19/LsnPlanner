package com.lczarny.lsnplanner.presentation.ui.planclass.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sports
import androidx.compose.ui.graphics.vector.ImageVector
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.model.PlanClassColor
import com.lczarny.lsnplanner.data.local.model.PlanClassType

fun LessonPlanType.planClassTypeLabelMap(context: Context): Map<PlanClassType, String> = when (this) {
    LessonPlanType.School -> mapOf(
        PlanClassType.Class to context.getString(R.string.class_type_class),
        PlanClassType.PE to context.getString(R.string.class_type_pe),
        PlanClassType.Other to context.getString(R.string.class_type_other)
    )

    LessonPlanType.University -> mapOf(
        PlanClassType.Lecture to context.getString(R.string.class_type_lecture),
        PlanClassType.Practical to context.getString(R.string.class_type_practical),
        PlanClassType.Laboratory to context.getString(R.string.class_type_laboratory),
        PlanClassType.Seminar to context.getString(R.string.class_type_seminar),
        PlanClassType.Workshop to context.getString(R.string.class_type_workshop),
        PlanClassType.Other to context.getString(R.string.class_type_other)
    )
}

fun PlanClassType.toPlanClassTypeIcon(): ImageVector = when (this) {
    PlanClassType.Class -> Icons.Filled.Class
    PlanClassType.PE -> Icons.Filled.Sports
    PlanClassType.Lecture -> Icons.AutoMirrored.Filled.LibraryBooks
    PlanClassType.Practical -> Icons.Filled.DesignServices
    PlanClassType.Laboratory -> Icons.Filled.Science
    PlanClassType.Seminar -> Icons.Filled.School
    PlanClassType.Workshop -> Icons.Filled.Handyman
    PlanClassType.Other -> Icons.Filled.Schedule
}

fun PlanClassColor.colorToLabel(context: Context): String = when (this) {
    PlanClassColor.Default -> context.getString(R.string.color_default)
    PlanClassColor.Red -> context.getString(R.string.color_red)
    PlanClassColor.Green -> context.getString(R.string.color_green)
    PlanClassColor.Purple -> context.getString(R.string.color_purple)
}
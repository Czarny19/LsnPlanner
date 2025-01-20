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
import com.lczarny.lsnplanner.data.local.model.PlanClassType

fun LessonPlanType.planClassTypeLabelMap(context: Context): Map<PlanClassType, String> = when (this) {
    LessonPlanType.School -> mapOf(
        Pair(PlanClassType.Class, context.getString(R.string.class_type_class)),
        Pair(PlanClassType.PE, context.getString(R.string.class_type_pe)),
        Pair(PlanClassType.Other, context.getString(R.string.class_type_other))
    )

    LessonPlanType.University -> mapOf(
        Pair(PlanClassType.Lecture, context.getString(R.string.class_type_lecture)),
        Pair(PlanClassType.Practical, context.getString(R.string.class_type_practical)),
        Pair(PlanClassType.Laboratory, context.getString(R.string.class_type_laboratory)),
        Pair(PlanClassType.Seminar, context.getString(R.string.class_type_seminar)),
        Pair(PlanClassType.Workshop, context.getString(R.string.class_type_workshop)),
        Pair(PlanClassType.Other, context.getString(R.string.class_type_other))
    )
}

val toPlanClassTypeIconMap: Map<PlanClassType, ImageVector> = mapOf(
    Pair(PlanClassType.Class, Icons.Filled.Class),
    Pair(PlanClassType.PE, Icons.Filled.Sports),
    Pair(PlanClassType.Lecture, Icons.AutoMirrored.Filled.LibraryBooks),
    Pair(PlanClassType.Practical, Icons.Filled.DesignServices),
    Pair(PlanClassType.Laboratory, Icons.Filled.Science),
    Pair(PlanClassType.Seminar, Icons.Filled.School),
    Pair(PlanClassType.Workshop, Icons.Filled.Handyman),
    Pair(PlanClassType.Other, Icons.Filled.Schedule)
)
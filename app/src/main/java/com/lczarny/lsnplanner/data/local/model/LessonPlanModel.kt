package com.lczarny.lsnplanner.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.entity.PlanClass

data class LessonPlanWithClasses(
    @Embedded val plan: LessonPlan,
    @Relation(parentColumn = "id", entityColumn = "lesson_plan_id") val classes: List<PlanClass>,
)

enum class LessonPlanType(val raw: String) {
    School("School"),
    University("University");

    companion object {
        fun from(find: String): LessonPlanType = LessonPlanType.entries.find { it.raw == find } ?: School
    }
}
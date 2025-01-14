package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.entity.PlanClass

data class LessonPlanModel(
    var id: Long? = null,
    var name: String,
    var type: LessonPlanType,
    @ColumnInfo(name = "create_date") var createDate: Long,
    @ColumnInfo(name = "isDefault") var isDefault: Boolean,
)

fun LessonPlan.mapToModel() = LessonPlanModel(
    id = this.id,
    name = this.name,
    type = this.type,
    createDate = this.createDate,
    isDefault = this.isDefault,
)

data class LessonPlanWithClasses(
    @Embedded val plan: LessonPlan,
    @Relation(parentColumn = "id", entityColumn = "lesson_plan_id") val classes: List<PlanClass>,
)

data class LessonPlanWithClassesModel(
    val plan: LessonPlanModel,
    val classes: List<PlanClassModel>,
)

fun LessonPlanWithClasses.mapToModel() = LessonPlanWithClassesModel(
    plan = plan.mapToModel(),
    classes = classes.map { it.mapToModel() },
)

enum class LessonPlanType(val raw: String) {
    School("School"),
    University("University");

    companion object {
        fun from(find: String): LessonPlanType = LessonPlanType.entries.find { it.raw == find } ?: School
    }
}
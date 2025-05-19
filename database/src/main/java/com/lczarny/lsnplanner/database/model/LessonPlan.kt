package com.lczarny.lsnplanner.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo

data class LessonPlan(
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "profile_id") var profileId: String,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: LessonPlanType = LessonPlanType.University,
    @ColumnInfo(name = "is_active") var isActive: Boolean = true,
    @ColumnInfo(name = "address_enabled") var addressEnabled: Boolean = true,
    @ColumnInfo(name = "create_date") val createDate: Long,
)

@Keep
enum class LessonPlanType(val raw: String) {
    School("School"),
    University("University");

    companion object {
        fun from(find: String): LessonPlanType = LessonPlanType.entries.find { it.raw == find } ?: School
    }
}

fun LessonPlanType.planClassTypes(): List<ClassType> = when (this) {
    LessonPlanType.School -> listOf(
        ClassType.Class,
        ClassType.Extracurricular,
        ClassType.Other
    )

    LessonPlanType.University -> listOf(
        ClassType.Lecture,
        ClassType.Practical,
        ClassType.Laboratory,
        ClassType.Seminar,
        ClassType.Workshop,
        ClassType.Other
    )
}

fun LessonPlanType.defaultClassType() = when (this) {
    LessonPlanType.School -> ClassType.Class
    LessonPlanType.University -> ClassType.Lecture
}
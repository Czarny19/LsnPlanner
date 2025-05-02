package com.lczarny.lsnplanner.data.common.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.LessonPlan

data class LessonPlanModel(
    val id: Long? = null,
    @ColumnInfo(name = "profile_id") var profileId: String,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: LessonPlanType = LessonPlanType.University,
    @ColumnInfo(name = "is_active") var isActive: Boolean = true,
    @ColumnInfo(name = "address_enabled") var addressEnabled: Boolean = true,
    @ColumnInfo(name = "create_date") val createDate: Long,
)

fun LessonPlan.toModel() = LessonPlanModel(
    id = this.id,
    profileId = this.profileId,
    name = this.name,
    type = this.type,
    isActive = this.isActive,
    addressEnabled = this.addressEnabled,
    createDate = this.createDate
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

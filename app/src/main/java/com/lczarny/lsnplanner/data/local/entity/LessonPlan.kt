package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Required

@Entity(tableName = "lesson_plan")
data class LessonPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @Required val name: String,
    @Required val type: LessonPlanType,
    @Required @ColumnInfo(name = "create_date") val createDate: LocalDateTime,
    @Required @ColumnInfo(name = "isDefault") val isDefault: Boolean,
)

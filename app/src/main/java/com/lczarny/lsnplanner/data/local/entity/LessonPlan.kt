package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import kotlinx.serialization.Required

@Entity(tableName = "lesson_plan")
data class LessonPlan(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "type", defaultValue = "University") val type: LessonPlanType,
    @Required @ColumnInfo(name = "is_active", defaultValue = "true") val isActive: Boolean,
    @Required @ColumnInfo(name = "address_enabled", defaultValue = "false") val addressEnabled: Boolean,
    @Required @ColumnInfo(name = "create_date", defaultValue = "CURRENT_TIMESTAMP") val createDate: Long,
)

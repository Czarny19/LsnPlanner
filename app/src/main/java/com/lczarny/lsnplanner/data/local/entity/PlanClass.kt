package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Required

@Entity(
    tableName = "plan_class", foreignKeys = [
        ForeignKey(
            entity = LessonPlan::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("lesson_plan_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlanClass(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required val name: String,
    @Required @ColumnInfo(name = "is_cyclical", defaultValue = "true") val isCyclical: Boolean,
    @ColumnInfo(defaultValue = "NULL") val note: String?,
    @ColumnInfo(name = "week_day", defaultValue = "NULL") val weekDay: Int?,
    @ColumnInfo(name = "start_time", defaultValue = "NULL") val startTime: Long?,
    @ColumnInfo(name = "end_time", defaultValue = "NULL") val endTime: Long?,
    @ColumnInfo(name = "single_date", defaultValue = "NULL") val singleDate: Long?,
    @ColumnInfo(name = "class_address", defaultValue = "NULL") val classAddress: String?,
    @ColumnInfo(name = "class_number", defaultValue = "NULL") val classNumber: String?,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
)
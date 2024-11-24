package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
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
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @Required val name: String,
    @Required @ColumnInfo(name = "is_cyclical") val isCyclical: Boolean,
    val note: String?,
    @ColumnInfo(name = "week_day") val weekDay: Int?,
    @ColumnInfo(name = "start_time") val startTime: LocalDateTime?,
    @ColumnInfo(name = "end_time") val endTime: LocalDateTime?,
    @ColumnInfo(name = "single_date") val singleDate: LocalDateTime?,
    @ColumnInfo(name = "class_address") val classAddress: String?,
    @ColumnInfo(name = "class_number") val classNumber: String?,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
)
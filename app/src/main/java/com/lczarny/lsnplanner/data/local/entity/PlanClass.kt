package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.local.model.PlanClassType
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
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "type") val type: PlanClassType,
    @Required @ColumnInfo(name = "color") val color: Long,
    @ColumnInfo(name = "note", defaultValue = "NULL") val note: String?,
    @ColumnInfo(name = "week_day", defaultValue = "NULL") val weekDay: Int?,
    @ColumnInfo(name = "start_date", defaultValue = "NULL") val startDate: Long?,
    @ColumnInfo(name = "start_hour") val startHour: Int,
    @ColumnInfo(name = "start_minute") val startMinute: Int,
    @Required @ColumnInfo(name = "duration_minutes", defaultValue = "45") val durationMinutes: Int,
    @Required @ColumnInfo(name = "classroom") val classroom: String,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
)
package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.local.model.ClassScheduleType
import kotlinx.serialization.Required

@Entity(
    tableName = "class_schedule",
    foreignKeys = [
        ForeignKey(
            entity = ClassInfo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("class_info_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ClassSchedule(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required @ColumnInfo(name = "class_info_id", index = true) val classInfoId: Long,
    @Required @ColumnInfo(name = "type") val type: ClassScheduleType,
    @Required @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,
    @Required @ColumnInfo(name = "classroom") val classroom: String,
    @ColumnInfo(name = "start_hour") val startHour: Int,
    @ColumnInfo(name = "start_minute") val startMinute: Int,
    @ColumnInfo(name = "week_day", defaultValue = "NULL") val weekDay: Int?,
    @ColumnInfo(name = "start_date", defaultValue = "NULL") val startDate: Long?,
    @ColumnInfo(name = "end_date", defaultValue = "NULL") val endDate: Long?,
)
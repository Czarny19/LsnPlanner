package com.lczarny.lsnplanner.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.database.model.ClassScheduleType
import kotlinx.serialization.Required

@Entity(
    tableName = "class_schedule",
    foreignKeys = [
        ForeignKey(
            entity = ClassInfoEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("class_info_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
internal data class ClassScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required @ColumnInfo(name = "class_info_id", index = true) val classInfoId: Long,
    @Required @ColumnInfo(name = "type") val type: ClassScheduleType,
    @Required @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,
    @Required @ColumnInfo(name = "classroom") val classroom: String,
    @ColumnInfo(name = "address", defaultValue = "NULL") val address: String?,
    @ColumnInfo(name = "start_hour") val startHour: Int,
    @ColumnInfo(name = "start_minute") val startMinute: Int,
    @ColumnInfo(name = "week_day", defaultValue = "NULL") val weekDay: Int?,
    @ColumnInfo(name = "start_date", defaultValue = "NULL") val startDate: Long?,
    @ColumnInfo(name = "end_date", defaultValue = "NULL") val endDate: Long?,
)
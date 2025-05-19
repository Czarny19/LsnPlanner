package com.lczarny.lsnplanner.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Ignore

data class ClassSchedule(
    @ColumnInfo(name = "id") var id: Long? = null,
    @Ignore var localTempId: Long? = null,
    @Ignore var state: ItemState = ItemState.Existing,
    @ColumnInfo(name = "class_info_id") var classInfoId: Long = -1L,
    @ColumnInfo(name = "type") var type: ClassScheduleType = ClassScheduleType.Weekly,
    @ColumnInfo(name = "duration_minutes") var durationMinutes: Int = 60,
    @ColumnInfo(name = "address") var address: String? = null,
    @ColumnInfo(name = "classroom") var classroom: String = "",
    @ColumnInfo(name = "start_hour") var startHour: Int = 8,
    @ColumnInfo(name = "start_minute") var startMinute: Int = 0,
    @ColumnInfo(name = "week_day") var weekDay: Int? = null,
    @ColumnInfo(name = "start_date") var startDate: Long? = null,
    @ColumnInfo(name = "end_date") var endDate: Long? = null,
)

data class ClassScheduleWithInfo(
    val schedule: ClassSchedule,
    val info: ClassInfo
)

@Keep
enum class ClassScheduleType(val raw: String) {
    Weekly("Weekly"),
    Period("Period"),
    Single("Single");

    companion object {
        fun from(find: String): ClassScheduleType = ClassScheduleType.entries.find { it.raw == find } ?: Weekly
    }
}
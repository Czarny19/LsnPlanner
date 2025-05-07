package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.lczarny.lsnplanner.data.local.entity.ClassSchedule

data class ClassScheduleModel(
    val id: Long? = null,
    @Ignore var localTempId: Long? = null,
    @Ignore var state: ItemState = ItemState.Existing,
    @ColumnInfo(name = "class_info_id") var classInfoId: Long,
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

fun ClassSchedule.toModel() = ClassScheduleModel(
    id = this.id,
    classInfoId = this.classInfoId,
    type = this.type,
    durationMinutes = this.durationMinutes,
    address = this.address,
    classroom = this.classroom,
    startHour = this.startHour,
    startMinute = this.startMinute,
    weekDay = this.weekDay,
    startDate = this.startDate,
    endDate = this.endDate
)

data class ClassScheduleWithInfoModel(
    val schedule: ClassScheduleModel,
    val info: ClassInfoModel
)

enum class ClassScheduleType(val raw: String) {
    Weekly("Weekly"),
    Period("Period"),
    Single("Single");

    companion object {
        fun from(find: String): ClassScheduleType = ClassScheduleType.entries.find { it.raw == find } ?: Weekly
    }
}
package com.lczarny.lsnplanner.database.model

import androidx.room.ColumnInfo

data class Note(
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "modify_date") var modifyDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)
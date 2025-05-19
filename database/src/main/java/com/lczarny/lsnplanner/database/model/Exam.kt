package com.lczarny.lsnplanner.database.model

import androidx.room.ColumnInfo

data class Exam(
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "class_info_id") val classInfoId: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "is_done") var isDone: Boolean = false,
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)
package com.lczarny.lsnplanner.database.model

import androidx.room.ColumnInfo

data class Homework(
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "class_info_id") val classInfoId: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "due_date") val dueDate: Long,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "done") var done: Boolean = false,
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)
package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.Exam

data class ExamModel(
    val id: Long? = null,
    @ColumnInfo(name = "class_info_id") val classInfoId: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "is_done") var isDone: Boolean = false,
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)

fun Exam.toModel() = ExamModel(
    id = this.id,
    classInfoId = this.classInfoId,
    name = this.name,
    date = this.date,
    description = this.description,
    importance = this.importance,
)
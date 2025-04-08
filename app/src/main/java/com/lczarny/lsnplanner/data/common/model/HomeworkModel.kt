package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.Homework

data class HomeworkModel(
    val id: Long? = null,
    @ColumnInfo(name = "class_info_id") val classInfoId: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "due_date") val dueDate: Long,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "done") var done: Boolean = false,
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)

fun Homework.toModel() = HomeworkModel(
    id = this.id,
    classInfoId = this.classInfoId,
    name = this.name,
    dueDate = this.dueDate,
    description = this.description,
    done = this.done,
    importance = this.importance,
)
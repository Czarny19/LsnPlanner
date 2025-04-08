package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.Note
import com.lczarny.lsnplanner.utils.currentTimestamp

data class NoteModel(
    val id: Long? = null,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "modify_date") var modifyDate: Long = currentTimestamp(),
    @ColumnInfo(name = "importance") var importance: Importance = Importance.Normal,
)

fun Note.toModel() = NoteModel(
    id = this.id,
    lessonPlanId = this.lessonPlanId,
    title = this.title,
    content = this.content,
    modifyDate = this.modifyDate,
    importance = this.importance,
)
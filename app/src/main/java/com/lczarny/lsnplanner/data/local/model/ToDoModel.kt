package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.ToDo

data class ToDoModel(
    var id: Long? = null,
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "historical") var historical: Boolean = false,
    @ColumnInfo(name = "importance") var importance: ToDoImportance = ToDoImportance.Low,
    @ColumnInfo(name = "due_date") var dueDate: Long? = null,
    @ColumnInfo(name = "class_id") var classId: Long? = null,
    @ColumnInfo(name = "lesson_plan_id") var lessonPlanId: Long,
)

fun ToDo.mapToModel() = ToDoModel(
    id = this.id,
    content = this.content,
    historical = this.historical,
    importance = this.importance,
    dueDate = this.dueDate,
    classId = this.classId,
    lessonPlanId = this.lessonPlanId,
)

enum class ToDoImportance(val raw: Int) {
    High(1),
    Medium(2),
    Low(3);

    companion object {
        fun from(find: Int): ToDoImportance = ToDoImportance.entries.find { it.raw == find } ?: Low
    }
}
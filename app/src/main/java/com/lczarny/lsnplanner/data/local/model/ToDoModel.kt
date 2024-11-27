package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.ToDo

data class ToDoModel(
    val id: Long? = null,
    val content: String,
    val historical: Boolean = false,
    val importance: ToDoImportance = ToDoImportance.Low,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "class_id") val classId: Long?,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
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
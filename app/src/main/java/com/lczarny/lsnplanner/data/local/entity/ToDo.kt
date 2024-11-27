package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.local.model.ToDoImportance
import kotlinx.serialization.Required

@Entity(
    tableName = "todo", foreignKeys = [
        ForeignKey(
            entity = PlanClass::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("class_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LessonPlan::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("lesson_plan_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required val content: String,
    @ColumnInfo(defaultValue = "false") val historical: Boolean,
    @ColumnInfo(defaultValue = "3") val importance: ToDoImportance,
    @ColumnInfo(name = "due_date", defaultValue = "NULL") val dueDate: Long?,
    @ColumnInfo(name = "class_id", index = true, defaultValue = "NULL") val classId: Long?,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
)

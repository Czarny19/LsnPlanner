package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
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
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    @Required val content: String,
    val historical: Boolean = false,
//    val importance: ,
    @ColumnInfo(name = "due_date") val dueDate: LocalDateTime? = null,
    @ColumnInfo(name = "class_id", index = true) val classId: Long? = null,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
)

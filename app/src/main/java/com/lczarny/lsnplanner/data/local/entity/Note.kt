package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.common.model.Importance
import kotlinx.serialization.Required

@Entity(
    tableName = "note",
    foreignKeys = [
        ForeignKey(
            entity = LessonPlan::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("lesson_plan_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
    @Required @ColumnInfo(name = "title") val title: String,
    @Required @ColumnInfo(name = "content") val content: String,
    @Required @ColumnInfo(name = "modify_date") val modifyDate: Long,
    @Required @ColumnInfo(name = "importance", defaultValue = "1") val importance: Importance,
)

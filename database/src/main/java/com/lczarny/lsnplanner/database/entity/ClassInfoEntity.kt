package com.lczarny.lsnplanner.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.database.model.ClassType
import kotlinx.serialization.Required

@Entity(
    tableName = "class_info",
    foreignKeys = [
        ForeignKey(
            entity = LessonPlanEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("lesson_plan_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
internal data class ClassInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @Required @ColumnInfo(name = "lesson_plan_id", index = true) val lessonPlanId: Long,
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "type") val type: ClassType,
    @Required @ColumnInfo(name = "color") val color: Long,
    @ColumnInfo(name = "address", defaultValue = "NULL") val address: String?,
    @ColumnInfo(name = "note", defaultValue = "NULL") val note: String?,
)
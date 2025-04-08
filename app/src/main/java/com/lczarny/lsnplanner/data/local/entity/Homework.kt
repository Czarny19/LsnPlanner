package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.common.model.Importance
import kotlinx.serialization.Required

@Entity(
    tableName = "homework",
    foreignKeys = [
        ForeignKey(
            entity = ClassInfo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("class_info_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Homework(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "class_info_id", index = true) val classInfoId: Long,
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "due_date") val dueDate: Long,
    @ColumnInfo(name = "description") val description: String,
    @Required @ColumnInfo(name = "done", defaultValue = "false") val done: Boolean,
    @Required @ColumnInfo(name = "importance", defaultValue = "1") val importance: Importance,
)
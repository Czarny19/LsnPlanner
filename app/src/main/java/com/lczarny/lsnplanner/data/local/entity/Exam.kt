package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.data.common.model.Importance
import kotlinx.serialization.Required

@Entity(
    tableName = "exam",
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
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "class_info_id", index = true) val classInfoId: Long,
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "description") val description: String,
    @Required @ColumnInfo(name = "importance", defaultValue = "1") val importance: Importance,
)
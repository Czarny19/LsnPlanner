package com.lczarny.lsnplanner.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lczarny.lsnplanner.database.model.LessonPlanType
import kotlinx.serialization.Required

@Entity(
    tableName = "lesson_plan",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("profile_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
internal data class LessonPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "profile_id", index = true) val profileId: String,
    @Required @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "type", defaultValue = "University") val type: LessonPlanType,
    @Required @ColumnInfo(name = "is_active", defaultValue = "true") val isActive: Boolean,
    @Required @ColumnInfo(name = "address_enabled", defaultValue = "false") val addressEnabled: Boolean,
    @Required @ColumnInfo(name = "create_date", defaultValue = "CURRENT_TIMESTAMP") val createDate: Long,
)
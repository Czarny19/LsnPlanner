package com.lczarny.lsnplanner.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
internal data class ProfileEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "joined", defaultValue = "CURRENT_TIMESTAMP") val joined: Long,
)
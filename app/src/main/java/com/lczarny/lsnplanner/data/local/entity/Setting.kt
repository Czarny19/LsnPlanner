package com.lczarny.lsnplanner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Required

@Entity(tableName = "setting")
data class Setting(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @Required @ColumnInfo(name = "value") val value: String,
)

package com.lczarny.lsnplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Required

@Entity(tableName = "setting")
data class Setting(
    @PrimaryKey val name: String,
    @Required val value: String,
)

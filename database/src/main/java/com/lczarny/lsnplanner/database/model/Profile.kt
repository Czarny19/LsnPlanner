package com.lczarny.lsnplanner.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo

@Immutable
data class Profile(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "joined") val joined: Long = System.currentTimeMillis(),
)
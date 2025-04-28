package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.Profile
import com.lczarny.lsnplanner.utils.currentTimestamp

data class ProfileModel(
    val id: Long,
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "joined") var joined: Long = currentTimestamp(),
)

fun Profile.toModel() = ProfileModel(
    id = this.id,
    email = this.email,
    joined = this.joined,
)
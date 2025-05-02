package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import com.lczarny.lsnplanner.data.local.entity.Profile
import com.lczarny.lsnplanner.utils.currentTimestamp
import io.github.jan.supabase.auth.user.UserInfo

data class ProfileModel(
    val id: String,
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "joined") var joined: Long = currentTimestamp(),
)

fun Profile.toModel() = ProfileModel(
    id = this.id,
    email = this.email,
    joined = this.joined,
)

fun UserInfo.toProfile() = ProfileModel(
    id = this.id,
    email = this.email!!,
    joined = this.createdAt?.toEpochMilliseconds()!!
)
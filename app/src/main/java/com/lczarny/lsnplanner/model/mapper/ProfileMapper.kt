package com.lczarny.lsnplanner.model.mapper

import com.lczarny.lsnplanner.database.model.Profile
import io.github.jan.supabase.auth.user.UserInfo

fun UserInfo.toProfile() = Profile(
    id = this.id,
    email = this.email!!,
    joined = this.createdAt?.toEpochMilliseconds()!!
)
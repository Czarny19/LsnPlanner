package com.lczarny.lsnplanner.model

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.model.Profile
import io.github.jan.supabase.auth.Auth

class SessionInfo(private val auth: Auth) {
    val status = auth.sessionStatus

    val user = { auth.currentUserOrNull() }

    var activeProfile = Profile(id = "")

    var activeLessonPlan = LessonPlan(id = -1L, profileId = "", createDate = 0L)
}
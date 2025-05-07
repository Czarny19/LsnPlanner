package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.ProfileModel
import io.github.jan.supabase.auth.Auth

class SessionRepository(private val auth: Auth) {
    val status = auth.sessionStatus

    val user = { auth.currentUserOrNull() }

    var activeProfile = ProfileModel(id = "")

    var activeLessonPlan = LessonPlanModel(id = -1L, profileId = "", createDate = 0L)
}
package com.lczarny.lsnplanner.presentation.navigation

import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import kotlinx.serialization.Serializable

@Serializable
object StartRoute

@Serializable
data class HomeRoute(val firstLaunch: Boolean = false)

@Serializable
object LessonPlanListRoute

@Serializable
data class LessonPlanRoute(val firstLaunch: Boolean = false, val lessonPlanId: Long? = null)

@Serializable
data class NoteRoute(val lessonPlanId: Long, val noteId: Long? = null)

@Serializable
data class ClassListRoute(val lessonPlanId: Long)

@Serializable
data class ClassDetailsRoute(
    val lessonPlanId: Long,
    val lessonPlanType: LessonPlanType,
    val defaultWeekDay: Int = 1,
    val classInfoId: Long? = null
)

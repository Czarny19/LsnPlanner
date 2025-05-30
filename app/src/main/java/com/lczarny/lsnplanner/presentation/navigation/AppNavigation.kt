package com.lczarny.lsnplanner.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignInRoute

@Serializable
object HomeRoute

@Serializable
object LessonPlanListRoute

@Serializable
data class LessonPlanRoute(val firstPlan: Boolean = false, val lessonPlanId: Long? = null)

@Serializable
data class NoteRoute(val noteId: Long? = null)

@Serializable
object ClassListRoute

@Serializable
data class ClassDetailsRoute(val defaultWeekDay: Int = 1, val classInfoId: Long? = null)

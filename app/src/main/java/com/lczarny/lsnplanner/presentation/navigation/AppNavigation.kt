package com.lczarny.lsnplanner.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object StartRoute

@Serializable
data class HomeRoute(val firstLaunch: Boolean = false)

@Serializable
data class LessonPlanRoute(val firstLaunch: Boolean = false, val lessonPlanId: Long? = null)

@Serializable
data class ToDoRoute(val lessonPlanId: Long, val toDoId: Long? = null)
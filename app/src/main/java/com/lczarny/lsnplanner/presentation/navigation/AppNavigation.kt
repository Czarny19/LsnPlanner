package com.lczarny.lsnplanner.presentation.navigation

import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import kotlinx.serialization.Serializable

@Serializable
object StartRoute

@Serializable
data class HomeRoute(val firstLaunch: Boolean = false)

@Serializable
data class LessonPlanRoute(val firstLaunch: Boolean = false, val lessonPlanId: Long? = null)

@Serializable
data class ToDoRoute(val lessonPlanId: Long, val classId: Long? = null, val toDoId: Long? = null)

@Serializable
data class PlanClassRoute(
    val lessonPlanId: Long,
    val lessonPlanType: LessonPlanType,
    val defaultWeekDay: Int = 1,
    val planClassId: Long? = null
)
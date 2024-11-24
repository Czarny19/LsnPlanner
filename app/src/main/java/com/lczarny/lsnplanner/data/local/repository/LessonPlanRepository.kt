package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClasses
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class LessonPlanRepository(private val lessonPlanDao: LessonPlanDao) {

    fun checkIfDefaultPlanExists(): Flow<Boolean> = lessonPlanDao.checkIfDefaultPlanExists()

    fun defaultLessonPlanWithClasses(): Flow<LessonPlanWithClasses> = lessonPlanDao.getDefaultLessonPlanWithClasses()

    fun lessonPlanWithClasses(id: Long): Flow<LessonPlanWithClasses> = lessonPlanDao.getLessonPlanWithClasses(id)

    suspend fun insert(lessonPlan: LessonPlan) {
        lessonPlanDao.insertLessonPlan(lessonPlan)
    }

    suspend fun delete(id: Long) {
        lessonPlanDao.deleteLessonPlan(VarArgsId(id))
    }
}
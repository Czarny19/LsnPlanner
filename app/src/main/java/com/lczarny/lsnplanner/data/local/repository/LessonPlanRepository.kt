package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClassesModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.mapToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonPlanRepository(private val lessonPlanDao: LessonPlanDao) {

    fun checkIfDefaultPlanExists(): Flow<Boolean> = lessonPlanDao.checkIfDefaultPlanExists()

    fun lessonPlan(id: Long): Flow<LessonPlanModel> = lessonPlanDao.getLessonPlan(id).map { it.mapToModel() }

    fun defaultLessonPlanWithClasses(): Flow<LessonPlanWithClassesModel> =
        lessonPlanDao.getDefaultLessonPlanWithClasses().map { it.mapToModel() }

    fun lessonPlanWithClasses(id: Long): Flow<LessonPlanWithClassesModel> =
        lessonPlanDao.getLessonPlanWithClasses(id).map { it.mapToModel() }

    suspend fun insert(lessonPlan: LessonPlanModel) {
        lessonPlanDao.insertLessonPlan(lessonPlan)
    }

    suspend fun delete(id: Long) {
        lessonPlanDao.deleteLessonPlan(VarArgsId(id))
    }
}
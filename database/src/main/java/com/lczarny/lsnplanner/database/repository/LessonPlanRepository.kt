package com.lczarny.lsnplanner.database.repository

import com.lczarny.lsnplanner.database.dao.LessonPlanDao
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class LessonPlanRepository(private val dao: LessonPlanDao) {

    suspend fun getById(id: Long): LessonPlan = dao.getSingle(id)

    fun watchActive(profileId: String) = dao.watchActive(profileId)

    fun watchAll(profileId: String): Flow<List<LessonPlan>> = dao.watchAll(profileId)

    suspend fun makeOtherPlansNotActive(lessonPlan: LessonPlan) {
        dao.makeOtherPlansNotActive(lessonPlanId = lessonPlan.id!!, profileId = lessonPlan.profileId)
    }

    suspend fun update(lessonPlan: LessonPlan) {
        dao.update(lessonPlan)
    }

    suspend fun insert(lessonPlan: LessonPlan): Long = dao.insert(lessonPlan)

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
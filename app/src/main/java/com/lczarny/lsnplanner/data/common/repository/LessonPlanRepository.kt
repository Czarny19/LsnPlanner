package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonPlanRepository(private val dao: LessonPlanDao) {

    suspend fun checkIfActivePlanExists(profileId: String): Boolean = dao.checkIfActivePlanExists(profileId)

    suspend fun getById(id: Long): LessonPlanModel = dao.getSingle(id).toModel()

    fun getActivePlan(profileId: String): Flow<LessonPlanModel?> = dao.getActive(profileId).map { profile -> profile?.toModel() }

    fun getAll(profileId: String): Flow<List<LessonPlanModel>> = dao.getAll(profileId).map { items -> items.map { it.toModel() } }

    suspend fun makeOtherPlansNotActive(lessonPlan: LessonPlanModel) {
        dao.makeOtherPlansNotActive(lessonPlanId = lessonPlan.id!!, profileId = lessonPlan.profileId)
    }

    suspend fun update(lessonPlan: LessonPlanModel) {
        dao.update(lessonPlan)
    }

    suspend fun insert(lessonPlan: LessonPlanModel): Long = dao.insert(lessonPlan)

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
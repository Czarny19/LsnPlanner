package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonPlanRepository(private val dao: LessonPlanDao) {

    suspend fun checkIfActivePlanExists(): Boolean = dao.checkIfActivePlanExists()

    suspend fun getById(id: Long): LessonPlanModel = dao.getSingle(id).toModel()

    fun getActive(): Flow<LessonPlanModel> = dao.getActive().map { it.toModel() }

    fun getAll(): Flow<List<LessonPlanModel>> = dao.getAll().map { items -> items.map { it.toModel() } }

    suspend fun makeOtherPlansNotActive(lessonPlanId: Long) {
        dao.makeOtherPlansNotActive(lessonPlanId)
    }

    suspend fun update(lessonPlan: LessonPlanModel) {
        dao.update(lessonPlan)
    }

    suspend fun insert(lessonPlan: LessonPlanModel): Long = dao.insert(lessonPlan)

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LessonPlanRepository(private val dao: LessonPlanDao) {

    private var _activeLessonPlan = MutableStateFlow(LessonPlanModel(createDate = 0L, profileId = -1L))

    suspend fun checkIfActivePlanExists(profileId: Long): Boolean = dao.checkIfActivePlanExists(profileId)

    suspend fun getById(id: Long): LessonPlanModel = dao.getSingle(id).toModel()

    fun getActivePlan(): StateFlow<LessonPlanModel> = _activeLessonPlan.asStateFlow()

    suspend fun loadActivePlan(profileId: Long) {
        _activeLessonPlan.update { dao.getActive(profileId).toModel() }
    }

    fun getAll(profileId: Long): Flow<List<LessonPlanModel>> = dao.getAll(profileId).map { items -> items.map { it.toModel() } }

    suspend fun makeOtherPlansNotActive(lessonPlan: LessonPlanModel) {
        dao.makeOtherPlansNotActive(lessonPlanId = lessonPlan.id!!, profileId = lessonPlan.profileId)
    }

    suspend fun update(lessonPlan: LessonPlanModel) {
        dao.update(lessonPlan).run { loadActivePlan(lessonPlan.profileId) }
    }

    suspend fun insert(lessonPlan: LessonPlanModel): Long = dao.insert(lessonPlan).let { newId ->
        loadActivePlan(lessonPlan.profileId)
        newId
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
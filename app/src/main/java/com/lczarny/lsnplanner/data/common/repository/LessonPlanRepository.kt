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

    private var _activeLessonPlan = MutableStateFlow(LessonPlanModel(createDate = 0L))

    suspend fun checkIfActivePlanExists(): Boolean = dao.checkIfActivePlanExists()

    suspend fun getById(id: Long): LessonPlanModel = dao.getSingle(id).toModel()

    suspend fun getActivePlan(reload: Boolean = false): StateFlow<LessonPlanModel> {
        if (_activeLessonPlan.value.id == null || reload) {
            _activeLessonPlan.update { dao.getActive().toModel() }
        }

        return _activeLessonPlan.asStateFlow()
    }

    fun getAll(): Flow<List<LessonPlanModel>> = dao.getAll().map { items -> items.map { it.toModel() } }

    suspend fun makeOtherPlansNotActive(lessonPlanId: Long) {
        dao.makeOtherPlansNotActive(lessonPlanId)
    }

    suspend fun update(lessonPlan: LessonPlanModel) {
        dao.update(lessonPlan).run { getActivePlan(true) }
    }

    suspend fun insert(lessonPlan: LessonPlanModel): Long = dao.insert(lessonPlan).let { newId ->
        getActivePlan(true)
        newId
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
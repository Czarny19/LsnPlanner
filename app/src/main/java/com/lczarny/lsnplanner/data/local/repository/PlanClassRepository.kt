package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.PlanClassDao
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.data.local.model.PlanClassWithToDosModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.mapToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlanClassRepository(private val planClassDao: PlanClassDao) {

    fun classWithToDos(id: Long): Flow<PlanClassWithToDosModel> = planClassDao.getClassWithToDos(id).map { it.mapToModel() }

    suspend fun insert(planClass: PlanClassModel) {
        planClassDao.insertClass(planClass)
    }

    suspend fun update(planClass: PlanClassModel) {
        planClassDao.updateClass(planClass)
    }

    suspend fun delete(id: Long) {
        planClassDao.deleteClass(VarArgsId(id))
    }
}
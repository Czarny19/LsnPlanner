package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.PlanClassDao
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.model.PlanClassWithToDos
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class PlanClassRepository(private val planClassDao: PlanClassDao) {

    fun classWithToDos(id: Long): Flow<PlanClassWithToDos> = planClassDao.getClassWithToDos(id)

    suspend fun insert(planClass: PlanClass) {
        planClassDao.insertClass(planClass)
    }

    suspend fun delete(id: Long) {
        planClassDao.deleteClass(VarArgsId(id))
    }
}
package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.ClassScheduleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClassScheduleRepository(private val dao: ClassScheduleDao) {

    fun getAllForClassInfo(classInfoId: Long): Flow<List<ClassScheduleModel>> =
        dao.getAllForClassInfo(classInfoId).map { items -> items.map { it.toModel() } }

    suspend fun insert(classTime: ClassScheduleModel): Long = dao.insert(classTime)

    suspend fun update(classTime: ClassScheduleModel) {
        dao.update(classTime)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
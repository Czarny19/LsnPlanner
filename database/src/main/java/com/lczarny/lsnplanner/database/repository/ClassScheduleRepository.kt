package com.lczarny.lsnplanner.database.repository

import com.lczarny.lsnplanner.database.dao.ClassScheduleDao
import com.lczarny.lsnplanner.database.model.ClassSchedule
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class ClassScheduleRepository(private val dao: ClassScheduleDao) {

    fun watchAll(lessonPlanId: Long): Flow<List<ClassSchedule>> = dao.watchAll(lessonPlanId)

    suspend fun insert(classTime: ClassSchedule): Long = dao.insert(classTime)

    suspend fun update(classTime: ClassSchedule) {
        dao.update(classTime)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
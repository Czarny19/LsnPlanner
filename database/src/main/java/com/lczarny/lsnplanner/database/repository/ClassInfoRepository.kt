package com.lczarny.lsnplanner.database.repository

import com.lczarny.lsnplanner.database.dao.ClassInfoDao
import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.model.FullClass
import com.lczarny.lsnplanner.database.model.VarArgsId
import com.lczarny.lsnplanner.database.model.toModel
import kotlinx.coroutines.flow.Flow

class ClassInfoRepository(private val dao: ClassInfoDao) {

    suspend fun getFullDataById(id: Long): FullClass = dao.getSingleFullData(id).toModel()

    fun watchAll(lessonPlanId: Long): Flow<List<ClassInfo>> = dao.watchAll(lessonPlanId)

    suspend fun insert(classInfo: ClassInfo): Long = dao.insert(classInfo)

    suspend fun update(classInfo: ClassInfo) {
        dao.update(classInfo)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
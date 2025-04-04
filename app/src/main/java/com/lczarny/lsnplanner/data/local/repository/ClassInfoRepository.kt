package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.ClassInfoDao
import com.lczarny.lsnplanner.data.local.model.ClassInfoModel
import com.lczarny.lsnplanner.data.local.model.FullClassDataModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClassInfoRepository(private val dao: ClassInfoDao) {

    suspend fun getFullDataById(id: Long): FullClassDataModel = dao.getSingleFullData(id).toModel()

    fun getAllForLessonPlan(lessonPlanId: Long): Flow<List<ClassInfoModel>> =
        dao.getAllForLessonPlan(lessonPlanId).map { items -> items.map { it.toModel() } }

    suspend fun insert(classInfo: ClassInfoModel): Long = dao.insert(classInfo)

    suspend fun update(classInfo: ClassInfoModel) {
        dao.update(classInfo)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
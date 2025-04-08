package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lczarny.lsnplanner.data.local.entity.ClassInfo
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.model.FullClassData
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassInfoDao {

    @Transaction
    @Query("SELECT * FROM class_info WHERE id = :id")
    suspend fun getSingleFullData(id: Long): FullClassData

    @Query("SELECT * FROM class_info WHERE lesson_plan_id = :lessonPlanId")
    fun getAllForLessonPlan(lessonPlanId: Long): Flow<List<ClassInfo>>

    @Insert(entity = ClassInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(classInfo: ClassInfoModel): Long

    @Update(entity = ClassInfo::class)
    suspend fun update(classInfo: ClassInfoModel)

    @Delete(entity = ClassInfo::class)
    suspend fun delete(vararg id: VarArgsId)
}
package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lczarny.lsnplanner.data.local.entity.ClassSchedule
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassScheduleDao {

    @Query("SELECT * FROM class_schedule WHERE class_info_id = :classInfoId")
    fun getAllForClassInfo(classInfoId: Long): Flow<List<ClassSchedule>>

    @Insert(entity = ClassSchedule::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classTime: ClassScheduleModel): Long

    @Update(entity = ClassSchedule::class)
    suspend fun update(classTime: ClassScheduleModel)

    @Delete(entity = ClassSchedule::class)
    suspend fun delete(vararg id: VarArgsId)
}
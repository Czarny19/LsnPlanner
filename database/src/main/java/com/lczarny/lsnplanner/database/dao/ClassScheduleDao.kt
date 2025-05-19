package com.lczarny.lsnplanner.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lczarny.lsnplanner.database.entity.ClassScheduleEntity
import com.lczarny.lsnplanner.database.model.ClassSchedule
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassScheduleDao {

    @Query("SELECT * FROM class_schedule WHERE class_info_id = (SELECT class_info_id FROM lesson_plan where id = :lessonPlanId LIMIT 1)")
    fun watchAll(lessonPlanId: Long): Flow<List<ClassSchedule>>

    @Insert(entity = ClassScheduleEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classTime: ClassSchedule): Long

    @Update(entity = ClassScheduleEntity::class)
    suspend fun update(classTime: ClassSchedule)

    @Delete(entity = ClassScheduleEntity::class)
    suspend fun delete(vararg id: VarArgsId)
}
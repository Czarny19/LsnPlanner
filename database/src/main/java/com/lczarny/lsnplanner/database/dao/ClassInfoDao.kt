package com.lczarny.lsnplanner.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lczarny.lsnplanner.database.entity.ClassInfoEntity
import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.model.FullClassEntity
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassInfoDao {

    @Transaction
    @Query("SELECT * FROM class_info WHERE id = :id")
    suspend fun getSingleFullData(id: Long): FullClassEntity

    @Query("SELECT * FROM class_info WHERE lesson_plan_id = :lessonPlanId")
    fun watchAll(lessonPlanId: Long): Flow<List<ClassInfo>>

    @Insert(entity = ClassInfoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(classInfo: ClassInfo): Long

    @Update(entity = ClassInfoEntity::class)
    suspend fun update(classInfo: ClassInfo)

    @Delete(entity = ClassInfoEntity::class)
    suspend fun delete(vararg id: VarArgsId)
}
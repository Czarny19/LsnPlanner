package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.model.PlanClassWithToDos
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanClassDao {

    @Transaction
    @Query("SELECT * FROM plan_class WHERE id = :id")
    fun getClassWithToDos(id: Long): Flow<PlanClassWithToDos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(lesson: PlanClass)

    @Delete(entity = PlanClass::class)
    suspend fun deleteClass(vararg id: VarArgsId)
}
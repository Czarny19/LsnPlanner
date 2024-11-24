package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lczarny.lsnplanner.data.local.entity.ToDo
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo WHERE lesson_plan_id = :lessonPlanId")
    fun getAllToDos(lessonPlanId: Long): Flow<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDo)

    @Delete(entity = ToDo::class)
    suspend fun deleteToDo(vararg id: VarArgsId)
}
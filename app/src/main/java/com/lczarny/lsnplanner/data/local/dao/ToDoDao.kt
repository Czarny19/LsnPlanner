package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lczarny.lsnplanner.data.local.entity.ToDo
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo WHERE lesson_plan_id = :lessonPlanId")
    fun getAllToDos(lessonPlanId: Long): Flow<List<ToDo>>

    @Insert(entity = ToDo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDoModel)

    @Update(entity = ToDo::class)
    suspend fun updateToDo(vararg toDo: ToDoModel)

    @Delete(entity = ToDo::class)
    suspend fun deleteToDo(vararg id: VarArgsId)

    @Query("DELETE FROM todo WHERE historical = 1")
    suspend fun deleteHistoricalToDos()

    @Query("UPDATE todo SET historical = 1 WHERE id = :id")
    suspend fun markToDoAsComplete(id: Long)
}
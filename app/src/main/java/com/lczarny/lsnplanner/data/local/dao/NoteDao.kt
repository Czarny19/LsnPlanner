package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lczarny.lsnplanner.data.local.entity.Note
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getSingle(noteId: Long): Note

    @Query("SELECT * FROM note WHERE lesson_plan_id = :lessonPlanId")
    fun getAllForLessonPlan(lessonPlanId: Long): Flow<List<Note>>

    @Insert(entity = Note::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteModel)

    @Update(entity = Note::class)
    suspend fun update(note: NoteModel)

    @Delete(entity = Note::class)
    suspend fun delete(vararg id: VarArgsId)
}
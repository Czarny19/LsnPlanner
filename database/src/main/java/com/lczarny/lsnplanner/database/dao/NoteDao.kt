package com.lczarny.lsnplanner.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lczarny.lsnplanner.database.entity.NoteEntity
import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getSingle(noteId: Long): Note

    @Query("SELECT * FROM note WHERE lesson_plan_id = :lessonPlanId ORDER BY importance desc, modify_date desc")
    fun watchAll(lessonPlanId: Long): Flow<List<Note>>

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update(entity = NoteEntity::class)
    suspend fun update(note: Note)

    @Delete(entity = NoteEntity::class)
    suspend fun delete(vararg id: VarArgsId)
}
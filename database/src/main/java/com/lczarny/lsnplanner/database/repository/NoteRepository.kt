package com.lczarny.lsnplanner.database.repository

import com.lczarny.lsnplanner.database.dao.NoteDao
import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.model.VarArgsId
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {

    suspend fun getById(noteId: Long): Note = dao.getSingle(noteId)

    fun watchAll(lessonPlanId: Long): Flow<List<Note>> = dao.watchAll(lessonPlanId)

    suspend fun insert(note: Note) {
        dao.insert(note)
    }

    suspend fun update(note: Note) {
        dao.update(note)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
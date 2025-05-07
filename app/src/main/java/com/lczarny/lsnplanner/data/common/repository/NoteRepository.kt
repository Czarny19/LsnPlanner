package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.data.common.model.VarArgsId
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val dao: NoteDao) {

    suspend fun getById(noteId: Long): NoteModel = dao.getSingle(noteId).toModel()

    fun watchAll(lessonPlanId: Long): Flow<List<NoteModel>> =
        dao.watchAll(lessonPlanId).map { items -> items.map { it.toModel() } }

    suspend fun insert(note: NoteModel) {
        dao.insert(note)
    }

    suspend fun update(note: NoteModel) {
        dao.update(note)
    }

    suspend fun delete(id: Long) {
        dao.delete(VarArgsId(id))
    }
}
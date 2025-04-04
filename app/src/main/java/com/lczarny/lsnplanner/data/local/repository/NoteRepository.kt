package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.NoteDao
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.data.local.model.VarArgsId
import com.lczarny.lsnplanner.data.local.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val dao: NoteDao) {

    suspend fun getById(noteId: Long): NoteModel = dao.getSingle(noteId).toModel()

    fun getAllForLessonPlan(lessonPlanId: Long): Flow<List<NoteModel>> =
        dao.getAllForLessonPlan(lessonPlanId).map { items -> items.map { it.toModel() } }

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
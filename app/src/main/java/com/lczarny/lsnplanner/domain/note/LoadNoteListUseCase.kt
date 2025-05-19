package com.lczarny.lsnplanner.domain.note

import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.repository.NoteRepository
import com.lczarny.lsnplanner.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadNoteListUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val noteRepository: NoteRepository
) {

    fun invoke(): Flow<List<Note>> = noteRepository.watchAll(sessionInfo.activeLessonPlan.id!!)
}
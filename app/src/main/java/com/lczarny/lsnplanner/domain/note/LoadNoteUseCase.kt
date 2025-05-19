package com.lczarny.lsnplanner.domain.note

import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.repository.NoteRepository
import com.lczarny.lsnplanner.model.SessionInfo
import javax.inject.Inject

class LoadNoteUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val noteRepository: NoteRepository
) {

    suspend fun invoke(noteId: Long?): Note =
        noteId?.let { id ->
            noteRepository.getById(id)
        } ?: run {
            Note(lessonPlanId = sessionInfo.activeLessonPlan.id!!)
        }
}
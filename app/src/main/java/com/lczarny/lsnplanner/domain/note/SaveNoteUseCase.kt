package com.lczarny.lsnplanner.domain.note

import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.repository.NoteRepository
import com.lczarny.lsnplanner.utils.currentTimestamp
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun invoke(note: Note) {
        note.id?.let {
            noteRepository.update(note.apply { modifyDate = currentTimestamp() })
        } ?: run {
            noteRepository.insert(note)
        }
    }
}
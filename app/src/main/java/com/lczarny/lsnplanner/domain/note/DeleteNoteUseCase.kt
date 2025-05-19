package com.lczarny.lsnplanner.domain.note

import com.lczarny.lsnplanner.database.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun invoke(id: Long) {
        noteRepository.delete(id)
    }
}
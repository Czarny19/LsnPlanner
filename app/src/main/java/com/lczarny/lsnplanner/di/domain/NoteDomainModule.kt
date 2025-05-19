package com.lczarny.lsnplanner.di.domain

import com.lczarny.lsnplanner.database.repository.NoteRepository
import com.lczarny.lsnplanner.domain.note.DeleteNoteUseCase
import com.lczarny.lsnplanner.domain.note.LoadNoteListUseCase
import com.lczarny.lsnplanner.domain.note.LoadNoteUseCase
import com.lczarny.lsnplanner.domain.note.SaveNoteUseCase
import com.lczarny.lsnplanner.model.SessionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NoteDomainModule {

    @Provides
    fun provideLoadNoteListUseCase(
        sessionRepository: SessionInfo,
        noteRepository: NoteRepository
    ): LoadNoteListUseCase = LoadNoteListUseCase(sessionRepository, noteRepository)

    @Provides
    fun provideLoadNoteUseCase(
        sessionRepository: SessionInfo,
        noteRepository: NoteRepository
    ): LoadNoteUseCase = LoadNoteUseCase(sessionRepository, noteRepository)

    @Provides
    fun provideSaveNoteUseCase(noteRepository: NoteRepository): SaveNoteUseCase = SaveNoteUseCase(noteRepository)

    @Provides
    fun provideDeleteNoteUseCase(noteRepository: NoteRepository): DeleteNoteUseCase = DeleteNoteUseCase(noteRepository)
}
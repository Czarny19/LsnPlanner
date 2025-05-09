package com.lczarny.lsnplanner.presentation.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.Importance
import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.NoteRepository
import com.lczarny.lsnplanner.data.common.repository.SessionRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.currentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionRepository: SessionRepository,
    private val noteRepository: NoteRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _note = MutableStateFlow<NoteModel?>(null)
    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private val _noteImportanceTutorialDone = MutableStateFlow(false)

    private lateinit var _initialData: NoteModel

    val screenState = _screenState.asStateFlow()

    val note = _note.asStateFlow()
    val dataChanged = _dataChanged.asStateFlow()
    val saveEnabled = _saveEnabled.asStateFlow()

    val noteImportanceTutorialDone = _noteImportanceTutorialDone.asStateFlow()

    init {
        watchSettings()
    }

    fun updateTitle(value: String) {
        _note.update { _note.value?.copy(title = value) }
        checkSaveEnabled()
        checkDataChanged()
    }

    fun updateContent(value: String) {
        _note.update { _note.value?.copy(content = value) }
        checkSaveEnabled()
        checkDataChanged()
    }

    fun updateImportance(value: Importance) {
        _note.update { _note.value?.copy(importance = value) }
        checkSaveEnabled()
        checkDataChanged()
    }

    fun intializeNote(noteId: Long?) {
        if (_note.value != null) {
            return
        }

        _screenState.update { DetailsScreenState.Loading }

        noteId?.let { id ->
            viewModelScope.launch(ioDispatcher) {
                noteRepository.getById(id).let { note ->
                    _note.update { note }
                    _initialData = note.copy()
                    _screenState.update { DetailsScreenState.Edit }
                }
            }
        } ?: run {
            NoteModel(lessonPlanId = sessionRepository.activeLessonPlan.id!!).let { note ->
                _note.update { note }
                _initialData = note.copy()
                _screenState.update { DetailsScreenState.Create }
            }
        }
    }

    fun saveNote() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            _note.value?.let { note ->
                note.id?.let {
                    noteRepository.update(note.apply { modifyDate = currentTimestamp() })
                } ?: run {
                    noteRepository.insert(note)
                }
            }
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    fun markNoteImportanceTutorialDone() {
        viewModelScope.launch(ioDispatcher) {
            _noteImportanceTutorialDone.update { true }
            dataStoreRepository.setTutorialNoteImportanceDone()
        }
    }

    private fun watchSettings() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppSettings().flowOn(ioDispatcher).collect { appSettings ->
                _noteImportanceTutorialDone.update { appSettings.tutorials.noteImportanceDone }
            }
        }
    }

    private fun checkSaveEnabled() {
        val titleNotEmpty = _note.value?.title?.isNotEmpty() == true
        val contentNotEmpty = _note.value?.content?.isNotEmpty() == true
        _saveEnabled.update { titleNotEmpty && contentNotEmpty }
    }

    private fun checkDataChanged() {
        _dataChanged.update { _initialData != note.value }
    }
}
package com.lczarny.lsnplanner.presentation.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.NoteImportance
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.data.local.repository.NoteRepository
import com.lczarny.lsnplanner.utils.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(NoteScreenState.Loading)
    private val _note = MutableStateFlow<NoteModel?>(null)

    private val _saveEnabled = MutableStateFlow(false)
    private val _formTouched = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val note = _note.asStateFlow()

    val saveEnabled = _saveEnabled.asStateFlow()
    val formTouched = _formTouched.asStateFlow()

    fun updateContent(value: String) {
        _note.tryEmit(_note.value?.copy(content = value))
        _saveEnabled.tryEmit(value.isNotEmpty())
        _formTouched.tryEmit(true)
    }

    fun updateImportance(value: NoteImportance) {
        _note.tryEmit(_note.value?.copy(importance = value))
        _saveEnabled.tryEmit(_note.value?.content?.isNotEmpty() ?: false)
        _formTouched.tryEmit(true)
    }

    fun intializeNote(lessonPlanId: Long, noteId: Long?) {
        _screenState.tryEmit(NoteScreenState.Loading)

        noteId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.getById(noteId).let { _note.emit(it) }
            }.invokeOnCompletion {
                _screenState.tryEmit(NoteScreenState.Edit)
            }
        } ?: run {
            _note.tryEmit(NoteModel(lessonPlanId = lessonPlanId))
            _screenState.tryEmit(NoteScreenState.Edit)
        }
    }

    fun saveNote() {
        _screenState.tryEmit(NoteScreenState.Saving)

        viewModelScope.launch(Dispatchers.IO) {
            _note.value?.let { note ->
                note.id?.let {
                    noteRepository.update(note.apply { modifyDate = getCurrentTimestamp() })
                } ?: run {
                    noteRepository.insert(note)
                }
            }
        }.invokeOnCompletion {
            _screenState.tryEmit(NoteScreenState.Finished)
        }
    }
}
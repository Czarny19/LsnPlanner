package com.lczarny.lsnplanner.presentation.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.NoteImportance
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.data.local.repository.NoteRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.currentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)
    private val _note = MutableStateFlow<NoteModel?>(null)

    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: NoteModel

    val screenState = _screenState.asStateFlow()
    val note = _note.asStateFlow()

    val saveEnabled = _saveEnabled.asStateFlow()

    val dataChanged = { _note.value != _initialData }

    fun updateContent(value: String) {
        _note.tryEmit(_note.value?.copy(content = value))
        _saveEnabled.tryEmit(value.isNotEmpty())
    }

    fun updateImportance(value: NoteImportance) {
        _note.tryEmit(_note.value?.copy(importance = value))
        _saveEnabled.tryEmit(_note.value?.content?.isNotEmpty() ?: false)
    }

    fun intializeNote(lessonPlanId: Long, noteId: Long?) {
        if (_note.value != null) {
            return
        }

        _screenState.tryEmit(DetailsScreenState.Loading)

        noteId?.let { id ->
            viewModelScope.launch(ioDispatcher) {
                noteRepository.getById(id).let {
                    _note.emit(it)
                    _initialData = it.copy()
                }
            }.invokeOnCompletion {
                _screenState.tryEmit(DetailsScreenState.Edit)
            }
        } ?: run {
            NoteModel(lessonPlanId = lessonPlanId).let {
                _note.tryEmit(it)
                _initialData = it.copy()
            }

            _screenState.tryEmit(DetailsScreenState.Edit)
        }
    }

    fun saveNote() {
        _screenState.tryEmit(DetailsScreenState.Saving)

        viewModelScope.launch(ioDispatcher) {
            _note.value?.let { note ->
                note.id?.let {
                    noteRepository.update(note.apply { modifyDate = currentTimestamp() })
                } ?: run {
                    noteRepository.insert(note)
                }
            }
        }.invokeOnCompletion {
            _screenState.tryEmit(DetailsScreenState.Finished)
        }
    }
}
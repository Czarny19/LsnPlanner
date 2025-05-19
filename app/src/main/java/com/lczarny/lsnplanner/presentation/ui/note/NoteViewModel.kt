package com.lczarny.lsnplanner.presentation.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.Importance
import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.datastore.GetAppSettingsUseCase
import com.lczarny.lsnplanner.domain.datastore.SetAppSettingUseCase
import com.lczarny.lsnplanner.domain.note.LoadNoteUseCase
import com.lczarny.lsnplanner.domain.note.SaveNoteUseCase
import com.lczarny.lsnplanner.model.DetailsScreenState
import com.lczarny.lsnplanner.model.Pref
import com.lczarny.lsnplanner.utils.updateIfChanged
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
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val setAppSettingUseCase: SetAppSettingUseCase,
    private val loadNoteUseCase: LoadNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _note = MutableStateFlow<Note?>(null)
    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private val _noteImportanceTutorialDone = MutableStateFlow(false)

    private lateinit var _initialData: Note

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

        viewModelScope.launch(ioDispatcher) {
            loadNoteUseCase.invoke(noteId).let { note ->
                _note.update { note }
                _initialData = note.copy()
            }
        }.invokeOnCompletion {
            _screenState.update { if (noteId == null) DetailsScreenState.Create else DetailsScreenState.Edit }
        }
    }

    fun saveNote() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            saveNoteUseCase.invoke(_note.value!!)
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    fun markNoteImportanceTutorialDone() {
        viewModelScope.launch(ioDispatcher) {
            _noteImportanceTutorialDone.update { true }
            setAppSettingUseCase.invoke(Pref.TutorialNoteImportance(), "true")
        }
    }

    private fun watchSettings() {
        viewModelScope.launch(ioDispatcher) {
            getAppSettingsUseCase.invoke().flowOn(ioDispatcher).collect { appSettings ->
                _noteImportanceTutorialDone.update { appSettings.tutorials.noteImportanceDone }
            }
        }
    }

    private fun checkSaveEnabled() {
        val titleNotEmpty = _note.value?.title?.isNotEmpty() == true
        val contentNotEmpty = _note.value?.content?.isNotEmpty() == true
        _saveEnabled.updateIfChanged(titleNotEmpty && contentNotEmpty)
    }

    private fun checkDataChanged() {
        _dataChanged.updateIfChanged(_initialData != note.value)
    }
}
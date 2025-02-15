package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.AppSetting
import com.lczarny.lsnplanner.data.local.model.ClassInfoModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.data.local.model.SettingModel
import com.lczarny.lsnplanner.data.local.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.repository.NoteRepository
import com.lczarny.lsnplanner.data.local.repository.SettingRepository
import com.lczarny.lsnplanner.presentation.components.closedBasicDialogState
import com.lczarny.lsnplanner.presentation.ui.home.model.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeScreenState.Loading)
    private val _firstLaunchDone = MutableStateFlow(false)

    private val _confirmDialogState = MutableStateFlow(closedBasicDialogState.copy())

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)

    private val _classes = MutableStateFlow(emptyList<ClassInfoModel>())
    private val _classesCurrentDate = MutableStateFlow(Calendar.getInstance())

    private val _notes = MutableStateFlow(emptyList<NoteModel>())
    private val _noteListSwipeTutorialDone = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val firstLaunchDone = _firstLaunchDone.asStateFlow()
    val confirmDialogState = _confirmDialogState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()

    val classes = _classes.asStateFlow()
    val classesCurrentDate = _classesCurrentDate.asStateFlow()

    val notes = _notes.asStateFlow()
    val noteListSwipeTutorialDone = _noteListSwipeTutorialDone.asStateFlow()

    init {
        getActiveLessonPlan()
        getSettings()
    }

    fun setFirstLaunchDone() {
        _firstLaunchDone.tryEmit(true)
    }

    fun changeCurrentClassesDate(date: Calendar) {
        _classesCurrentDate.tryEmit(Calendar.getInstance().apply { timeInMillis = date.timeInMillis })
    }

    fun changeCurrentClassesWeek(goForward: Boolean) {
        _classesCurrentDate.tryEmit(
            Calendar.getInstance().apply {
                timeInMillis = _classesCurrentDate.value.timeInMillis
                set(Calendar.WEEK_OF_YEAR, get(Calendar.WEEK_OF_YEAR) + (if (goForward) 1 else -1))
                set(Calendar.DAY_OF_WEEK, 2)
            }
        )
    }

    fun setConfirmationDialogState(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
        _confirmDialogState.tryEmit(
            _confirmDialogState.value.apply {
                this.title = title
                this.text = text
                this.onDismiss = {
                    onDismiss.invoke()
                    _confirmDialogState.tryEmit(closedBasicDialogState.copy())
                }
                this.onConfirm = onConfirm
            }
        )
    }

    private fun getActiveLessonPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.getActive().flowOn(Dispatchers.IO).collect { plan ->
                _lessonPlan.emit(plan)

                plan.id?.let { id ->
                    getClasses(id)
                    getNotes(id)
                }

                _screenState.emit(HomeScreenState.Ready)
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.getSettingValue(AppSetting.NoteListSwipeTutorialDone).flowOn(Dispatchers.IO).collect { setting ->
                _noteListSwipeTutorialDone.update { setting.toBoolean() }
            }
        }
    }

    private fun getClasses(lessonPlanId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            classInfoRepository.getAllForLessonPlan(lessonPlanId).flowOn(Dispatchers.IO).collect { _classes.emit(it) }
        }
    }

    private fun getNotes(lessonPlanId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllForLessonPlan(lessonPlanId).flowOn(Dispatchers.IO).collect { _notes.emit(it) }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.delete(noteId)
        }.invokeOnCompletion {
            getNotes(_lessonPlan.value?.id!!)
        }
    }

    fun markNoteListSwipeTutorialDone() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.insert(SettingModel(name = AppSetting.NoteListSwipeTutorialDone.raw, value = "true"))
        }
    }

    fun resetTutorials(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.resetTutorialSettings()
        }.invokeOnCompletion {
            onComplete.invoke()
        }
    }
}
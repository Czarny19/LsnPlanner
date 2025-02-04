package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.AppSetting
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClassesModel
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.data.local.model.SettingModel
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.repository.SettingRepository
import com.lczarny.lsnplanner.data.local.repository.ToDoRepository
import com.lczarny.lsnplanner.presentation.components.closedConfirmationDialogState
import com.lczarny.lsnplanner.presentation.ui.home.model.HomeState
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
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeState.Loading)
    private val _firstLaunchDone = MutableStateFlow(false)

    private val _confirmDialogState = MutableStateFlow(closedConfirmationDialogState.copy())

    private val _lessonPlan = MutableStateFlow<LessonPlanWithClassesModel?>(null)

    private val _planClasses = MutableStateFlow(emptyList<PlanClassModel>())
    private val _planClassesCurrentDate = MutableStateFlow(Calendar.getInstance())

    private val _showHistoricalToDos = MutableStateFlow(false)
    private val _toDos = MutableStateFlow(emptyList<ToDoModel>())

    private val _todoListSwipeTutorialDone = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val firstLaunchDone = _firstLaunchDone.asStateFlow()
    val confirmDialogState = _confirmDialogState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()

    val planClasses = _planClasses.asStateFlow()
    val planClassesCurrentDate = _planClassesCurrentDate.asStateFlow()

    val showHistoricalToDos = _showHistoricalToDos.asStateFlow()
    val toDos = _toDos.asStateFlow()

    val todoListSwipeTutorialDone = _todoListSwipeTutorialDone.asStateFlow()

    init {
        getDefaultLessonPlan()
        getSettings()
    }

    fun setFirstLaunchDone() {
        _firstLaunchDone.update { true }
    }

    fun changeCurrentClassesDate(date: Calendar) {
        _planClassesCurrentDate.update { Calendar.getInstance().apply { timeInMillis = date.timeInMillis } }
    }

    fun changeCurrentClassesWeek(goForward: Boolean) {
        _planClassesCurrentDate.update {
            Calendar.getInstance().apply {
                timeInMillis = _planClassesCurrentDate.value.timeInMillis
                set(Calendar.WEEK_OF_YEAR, get(Calendar.WEEK_OF_YEAR) + (if (goForward) 1 else -1))
                set(Calendar.DAY_OF_WEEK, 2)
            }
        }
    }

    fun switchShowHistoricalToDos() {
        _showHistoricalToDos.update { !_showHistoricalToDos.value }
    }

    fun setConfirmationDialogState(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
        _confirmDialogState.update {
            _confirmDialogState.value.apply {
                this.title = title
                this.text = text
                this.onDismiss = {
                    onDismiss.invoke()
                    _confirmDialogState.update { closedConfirmationDialogState.copy() }
                }
                this.onConfirm = onConfirm
            }
        }
    }

    private fun getDefaultLessonPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.defaultLessonPlanWithClasses().flowOn(Dispatchers.IO).collect { plan ->
                _lessonPlan.update { plan }
                _planClasses.update { plan.classes }

                getToDos(plan.plan.id!!)

                _screenState.update { HomeState.Ready }
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.settingValue(AppSetting.TodoListSwipeTutorialDone).flowOn(Dispatchers.IO).collect { setting ->
                _todoListSwipeTutorialDone.update { setting.toBoolean() }
            }
        }
    }

    private fun getToDos(lessonPlanId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.allToDos(lessonPlanId).flowOn(Dispatchers.IO).collect { toDos ->
                _toDos.update { toDos }
            }
        }
    }

    fun markToDoAsComplete(toDoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.markAsComplete(toDoId)
        }.invokeOnCompletion {
            getToDos(_lessonPlan.value!!.plan.id!!)
        }
    }

    fun deleteToDo(toDoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.delete(toDoId)
        }.invokeOnCompletion {
            getToDos(_lessonPlan.value!!.plan.id!!)
        }
    }

    fun deleteAllHistoricalToDos(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAllHistorical()
        }.invokeOnCompletion {
            getToDos(_lessonPlan.value!!.plan.id!!)
            onComplete.invoke()
        }
    }

    fun markTodoListSwipeTutorialDone() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.insert(SettingModel(name = AppSetting.TodoListSwipeTutorialDone.raw, value = "true"))
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
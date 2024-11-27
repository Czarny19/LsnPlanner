package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.LessonPlanWithClassesModel
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.repository.ToDoRepository
import com.lczarny.lsnplanner.presentation.ui.home.model.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<HomeState>(HomeState.Loading)
    private val _firstLaunchDone = MutableStateFlow<Boolean>(false)

    private val _lessonPlan = MutableStateFlow<LessonPlanWithClassesModel?>(null)
    private val _todos = MutableStateFlow<List<ToDoModel>>(emptyList<ToDoModel>())

    val screenState = _screenState.asStateFlow()
    val firstLaunchDone = _firstLaunchDone.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()
    val todos = _todos.asStateFlow()

    init {
        getDefaultLessonPlan()
    }

    fun setFirstLaunchDone() {
        _firstLaunchDone.update { true }
    }

    private fun getDefaultLessonPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.defaultLessonPlanWithClasses().flowOn(Dispatchers.IO).collect { plan ->
                _lessonPlan.update { plan }
                getToDos(plan.plan.id!!)
            }
        }
    }

    private fun getToDos(lessonPlanId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.allToDos(lessonPlanId).flowOn(Dispatchers.IO).collect { toDos ->
                _todos.update { toDos }
                _screenState.update { HomeState.Ready }
            }
        }
    }
}
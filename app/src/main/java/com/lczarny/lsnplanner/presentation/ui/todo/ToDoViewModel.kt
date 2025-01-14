package com.lczarny.lsnplanner.presentation.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.ToDoImportance
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.data.local.repository.ToDoRepository
import com.lczarny.lsnplanner.presentation.ui.todo.model.ToDoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<ToDoState>(ToDoState.Loading)
    private val _lessonPlanId = MutableStateFlow<Long>(-1L)
    private val _toDoData = MutableStateFlow<ToDoModel?>(null)

    val screenState = _screenState.asStateFlow()
    val lessonPlanId = _lessonPlanId.asStateFlow()
    val toDoData = _toDoData.asStateFlow()

    fun updateContent(value: String) {
        _toDoData.update { _toDoData.value?.copy(content = value) }
    }

    fun updateDueDate(value: Long?) {
        _toDoData.update { _toDoData.value?.copy(dueDate = value) }
    }

    fun updateImportance(value: ToDoImportance) {
        _toDoData.update { _toDoData.value?.copy(importance = value) }
    }

    fun intializeToDo(lessonPlanId: Long, classId: Long?, toDo: ToDoModel?) {
        _lessonPlanId.update { lessonPlanId }

        if (toDo != null) {
            _toDoData.update { toDo }
        } else {
            _toDoData.update {
                ToDoModel(
                    lessonPlanId = lessonPlanId,
                    classId = classId,
                    content = "",
                )
            }
        }

        _screenState.update { ToDoState.Edit }
    }

    fun saveToDo() {
        _screenState.update { ToDoState.Saving }

        viewModelScope.launch(Dispatchers.IO) {
            _toDoData.value?.let {
                if (it.id != null) {
                    toDoRepository.update(it)
                } else {
                    toDoRepository.insert(it)
                }
            }
        }.invokeOnCompletion {
            _screenState.update { ToDoState.Finished }
        }
    }
}
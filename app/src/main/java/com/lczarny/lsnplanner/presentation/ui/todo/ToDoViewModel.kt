package com.lczarny.lsnplanner.presentation.ui.todo

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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

    val screenState = _screenState.asStateFlow()

    var lessonPlanId = mutableLongStateOf(-1L)
        private set

    var classId = mutableStateOf<Long?>(null)
        private set

    var toDoId = mutableLongStateOf(-1L)
        private set

    var content = mutableStateOf("")
        private set

    var dueDate = mutableStateOf<Long?>(null)
        private set

    var importance = mutableStateOf<ToDoImportance>(ToDoImportance.Low)
        private set

    fun updateContent(value: String) {
        content.value = value
    }

    fun updateDueDate(value: Long?) {
        dueDate.value = value
    }

    fun updateImportance(value: ToDoImportance) {
        importance.value = value
    }

    fun intializeToDo(lessonPlanId: Long, classId: Long?, toDo: ToDoModel?) {
        this.lessonPlanId.longValue = lessonPlanId
        this.classId.value = classId

        when (toDo) {
            null -> {
                _screenState.update { ToDoState.Edit }
            }

            else -> {
                toDo.let {
                    toDoId.longValue = it.id!!
                    content.value = it.content
                    dueDate.value = it.dueDate
                    importance.value = it.importance
                }

                _screenState.update { ToDoState.Edit }
            }
        }
    }

    fun saveToDo() {
        _screenState.value = ToDoState.Saving

        viewModelScope.launch(Dispatchers.IO) {
            if (toDoId.longValue >= 0) {
                ToDoModel(
                    id = toDoId.longValue,
                    content = content.value,
                    dueDate = dueDate.value,
                    historical = false,
                    importance = importance.value,
                    lessonPlanId = lessonPlanId.longValue,
                    classId = classId.value
                ).run {
                    toDoRepository.update(this)
                }
            } else {
                ToDoModel(
                    content = content.value,
                    dueDate = dueDate.value,
                    importance = importance.value,
                    historical = false,
                    lessonPlanId = lessonPlanId.longValue,
                    classId = classId.value
                ).run {
                    toDoRepository.insert(this)
                }
            }
        }.invokeOnCompletion {
            _screenState.value = ToDoState.Finished
        }
    }
}
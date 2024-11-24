package com.lczarny.lsnplanner.presentation.ui.todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.entity.ToDo
import com.lczarny.lsnplanner.presentation.ui.todo.model.ToDoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(

) : ViewModel() {

    private val _screenState = MutableStateFlow<ToDoState>(ToDoState.Loading)
    private val _toDo = MutableStateFlow<ToDo?>(null)

    val screenState = _screenState.asStateFlow()

    var isEdit = mutableStateOf(false)
        private set

    var content = mutableStateOf("")
        private set

    var contentError = mutableStateOf(false)
        private set

    var dueDate = mutableStateOf<Long?>(null)
        private set

    fun updateContent(value: String) {
        content.value = value
        contentError.value = false
    }

    fun updateDueDate(value: Long?) {
        dueDate.value = value
    }

    fun intializeToDo(toDo: ToDo?) {
        when (toDo) {
            null -> {
                _screenState.update { ToDoState.Edit }
            }

            else -> {
                _toDo.update { toDo }
                isEdit.value = true
                content.value = toDo.content
//                dueDate.value = toDo.dueDate
                _screenState.update { ToDoState.Edit }
            }
        }
    }

    fun saveToDo(lessonPlanId: Long) {
        if (content.value.isEmpty()) {
//            planNameError.value = true
            return
        }

        _screenState.value = ToDoState.Saving

        viewModelScope.launch(Dispatchers.IO) {
            if (isEdit.value) {

            } else {
                val toDo = ToDo(
                    content = content.value,
//                    dueDate = dueDate.value,
                    dueDate = null,
                    lessonPlanId = lessonPlanId,
                    classId = null
                )
            }
        }.invokeOnCompletion {
            _screenState.value = ToDoState.Finished
        }
    }
}
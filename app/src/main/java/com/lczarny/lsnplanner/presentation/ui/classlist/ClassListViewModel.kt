package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.cls.DeleteClassUseCase
import com.lczarny.lsnplanner.domain.cls.LoadClassListUseCase
import com.lczarny.lsnplanner.model.BasicScreenState
import com.lczarny.lsnplanner.model.SessionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassListViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionInfo: SessionInfo,
    private val loadClassListUseCase: LoadClassListUseCase,
    private val deleteClassUseCase: DeleteClassUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)

    private val _lessonPlanName = MutableStateFlow<String>("")
    private val _classes = MutableStateFlow(emptyList<ClassInfo>())

    private val _selectedClassName = MutableStateFlow("")

    val screenState = _screenState.asStateFlow()
    val lessonPlanName = _lessonPlanName.asStateFlow()
    val classes = _classes.asStateFlow()

    val selectedClassName = _selectedClassName.asStateFlow()

    init {
        _lessonPlanName.update { sessionInfo.activeLessonPlan.name }
        watchClasses()
    }

    fun deleteClass(classInfo: ClassInfo, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            deleteClassUseCase.invoke(classInfo.id!!)
        }.invokeOnCompletion {
            _selectedClassName.update { classInfo.name }
            onFinished.invoke()
        }
    }

    private fun watchClasses() {
        viewModelScope.launch(ioDispatcher) {
            loadClassListUseCase.invoke().flowOn(ioDispatcher).collect { classes ->
                _classes.update { classes }
                _screenState.update { BasicScreenState.Ready }
            }
        }
    }
}
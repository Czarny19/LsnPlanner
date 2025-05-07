package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.SessionRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.BasicScreenState
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
    private val sessionRepository: SessionRepository,
    private val classInfoRepository: ClassInfoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)

    private val _lessonPlanName = MutableStateFlow<String>("")
    private val _classes = MutableStateFlow(emptyList<ClassInfoModel>())

    private val _selectedClassName = MutableStateFlow("")

    val screenState = _screenState.asStateFlow()
    val lessonPlanName = _lessonPlanName.asStateFlow()
    val classes = _classes.asStateFlow()

    val selectedClassName = _selectedClassName.asStateFlow()

    init {
        _lessonPlanName.update { sessionRepository.activeLessonPlan.name }

        watchClasses()
    }

    fun watchClasses() {
        viewModelScope.launch(ioDispatcher) {
            classInfoRepository.watchAll(sessionRepository.activeLessonPlan.id!!).flowOn(ioDispatcher).collect { classes ->
                _classes.update { classes }
                _screenState.update { BasicScreenState.Ready }
            }
        }
    }

    fun setSelectedClassName(name: String) {
        _selectedClassName.update { name }
    }

    fun deleteClass(classInfoId: Long, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            classInfoRepository.delete(classInfoId)
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }
}
package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
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
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)
    private val _lessonPlanName = MutableStateFlow<String>("")
    private val _classes = MutableStateFlow(emptyList<ClassInfoModel>())

    val screenState = _screenState.asStateFlow()
    val lessonPlanName = _lessonPlanName.asStateFlow()
    val classes = _classes.asStateFlow()

    init {
        loadClasses()
    }

    fun loadClasses() {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.getActivePlan().collect { lessonPlan ->
                _lessonPlanName.update { lessonPlan.name }

                classInfoRepository.getAllForLessonPlan(lessonPlan.id!!).flowOn(ioDispatcher).collect { classes ->
                    _classes.update { classes }
                    _screenState.update { BasicScreenState.Ready }
                }
            }
        }
    }

    fun deleteClass(classInfoId: Long, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            classInfoRepository.delete(classInfoId)
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }
}
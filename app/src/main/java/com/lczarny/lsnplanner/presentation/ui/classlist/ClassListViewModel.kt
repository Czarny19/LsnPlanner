package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.ClassInfoModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.ListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassListViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(ListScreenState.Loading)
    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)
    private val _classes = MutableStateFlow(emptyList<ClassInfoModel>())

    val screenState = _screenState.asStateFlow()
    val lessonPlan = _lessonPlan.asStateFlow()
    val classes = _classes.asStateFlow()

    fun initializeLessonPlanAndClasses(lessonPlanId: Long) {
        viewModelScope.launch(ioDispatcher) {
            _lessonPlan.emit(lessonPlanRepository.getById(lessonPlanId))

            classInfoRepository.getAllForLessonPlan(lessonPlanId).flowOn(ioDispatcher).collect { classes ->
                _classes.emit(classes)
                _screenState.emit(ListScreenState.List)
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
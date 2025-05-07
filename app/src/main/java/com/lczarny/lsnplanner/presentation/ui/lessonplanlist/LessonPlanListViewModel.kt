package com.lczarny.lsnplanner.presentation.ui.lessonplanlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
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
class LessonPlanListViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionRepository: SessionRepository,
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)
    private val _lessonPlans = MutableStateFlow(emptyList<LessonPlanModel>())

    private val _selectedPlanName = MutableStateFlow("")

    val screenState = _screenState.asStateFlow()
    val lessonPlans = _lessonPlans.asStateFlow()

    val selectedPlanName = _selectedPlanName.asStateFlow()

    init {
        watchLessonPlans()
    }

    private fun watchLessonPlans() {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.watchAll(sessionRepository.activeProfile.id).flowOn(ioDispatcher).collect { plans ->
                _lessonPlans.update { plans }
                _screenState.update { BasicScreenState.Ready }
            }
        }
    }

    fun setSelectedPlanName(name: String) {
        _selectedPlanName.update { name }
    }

    fun makePlanActive(lessonPlan: LessonPlanModel, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.update(lessonPlan.apply { isActive = true }).also {
                lessonPlanRepository.makeOtherPlansNotActive(lessonPlan)
            }
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }

    fun deletePlan(lessonPlanId: Long, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.delete(lessonPlanId)
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }
}
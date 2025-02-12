package com.lczarny.lsnplanner.presentation.ui.lessonplanlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.model.ListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlanListViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(ListScreenState.Loading)
    private val _lessonPlans = MutableStateFlow(emptyList<LessonPlanModel>())

    val screenState = _screenState.asStateFlow()
    val lessonPlans = _lessonPlans.asStateFlow()

    init {
        loadLessonPlans()
    }

    private fun loadLessonPlans() {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.getAll().flowOn(Dispatchers.IO).collect { plans ->
                _lessonPlans.emit(plans)
                _screenState.emit(ListScreenState.List)
            }
        }
    }

    fun makePlanActive(lessonPlan: LessonPlanModel, onFinished: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.update(lessonPlan.apply { isActive = true }).also {
                lessonPlanRepository.makeOtherPlansNotActive(lessonPlan.id!!)
            }
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }

    fun deletePlan(lessonPlanId: Long, onFinished: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.delete(lessonPlanId)
        }.invokeOnCompletion {
            onFinished.invoke()
        }
    }
}
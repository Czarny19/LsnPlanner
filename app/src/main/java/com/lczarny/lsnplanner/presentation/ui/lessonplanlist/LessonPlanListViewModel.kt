package com.lczarny.lsnplanner.presentation.ui.lessonplanlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.plan.DeleteLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.LoadLessonPlanListUseCase
import com.lczarny.lsnplanner.domain.plan.SetLessonPlanActiveUseCase
import com.lczarny.lsnplanner.model.BasicScreenState
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
    private val loadLessonPlanListUseCase: LoadLessonPlanListUseCase,
    private val setLessonPlanActiveUseCase: SetLessonPlanActiveUseCase,
    private val deleteLessonPlanUseCase: DeleteLessonPlanUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)
    private val _lessonPlans = MutableStateFlow(emptyList<LessonPlan>())

    private val _selectedPlanName = MutableStateFlow("")

    val screenState = _screenState.asStateFlow()
    val lessonPlans = _lessonPlans.asStateFlow()

    val selectedPlanName = _selectedPlanName.asStateFlow()

    init {
        watchLessonPlans()
    }

    fun makePlanActive(lessonPlan: LessonPlan, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            setLessonPlanActiveUseCase.invoke(lessonPlan)
        }.invokeOnCompletion {
            _selectedPlanName.update { lessonPlan.name }
            onFinished.invoke()
        }
    }

    fun deletePlan(lessonPlan: LessonPlan, onFinished: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            deleteLessonPlanUseCase.invoke(lessonPlan.id!!)
        }.invokeOnCompletion {
            _selectedPlanName.update { lessonPlan.name }
            onFinished.invoke()
        }
    }

    private fun watchLessonPlans() {
        viewModelScope.launch(ioDispatcher) {
            loadLessonPlanListUseCase.invoke().flowOn(ioDispatcher).collect { plans ->
                _lessonPlans.update { plans }
                _screenState.update { BasicScreenState.Ready }
            }
        }
    }
}
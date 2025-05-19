package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.model.LessonPlanType
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.plan.LoadLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.SaveLessonPlanUseCase
import com.lczarny.lsnplanner.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.updateIfChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlanViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val loadLessonPlanUseCase: LoadLessonPlanUseCase,
    private val saveLessonPlanUseCase: SaveLessonPlanUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _lessonPlan = MutableStateFlow<LessonPlan?>(null)
    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: LessonPlan

    val screenState = _screenState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()
    val dataChanged = _dataChanged.asStateFlow()
    val saveEnabled = _saveEnabled.asStateFlow()

    fun updatePlanName(value: String) {
        _lessonPlan.update { _lessonPlan.value?.copy(name = value) }
        _saveEnabled.update { value.isNotEmpty() }
        checkDataChanged()
    }

    fun updatePlanType(value: LessonPlanType) {
        _lessonPlan.update { _lessonPlan.value?.copy(type = value) }
        checkDataChanged()
    }

    fun updateAddressEnabled(value: Boolean) {
        _lessonPlan.update { _lessonPlan.value?.copy(addressEnabled = value) }
        checkDataChanged()
    }

    fun setPlanAsActive() {
        _lessonPlan.update { _lessonPlan.value?.copy(isActive = true) }
        checkDataChanged()
    }

    fun initializePlan(planId: Long?) {
        if (_lessonPlan.value != null) {
            return
        }

        _screenState.update { DetailsScreenState.Loading }

        viewModelScope.launch(ioDispatcher) {
            loadLessonPlanUseCase.invoke(planId).let { lessonPlan ->
                _lessonPlan.update { lessonPlan }
                _saveEnabled.update { lessonPlan.name.isNotEmpty() }
                _initialData = lessonPlan.copy()
            }
        }.invokeOnCompletion {
            _screenState.update { if (planId == null) DetailsScreenState.Create else DetailsScreenState.Edit }
        }
    }

    fun savePlan() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            saveLessonPlanUseCase.invoke(_lessonPlan.value!!)
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    private fun checkDataChanged() {
        _dataChanged.updateIfChanged(_initialData != lessonPlan.value)
    }
}
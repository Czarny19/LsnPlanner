package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.GradingSystem
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlanViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)
    private val _isNewPlan = MutableStateFlow(false)

    private val _saveEnabled = MutableStateFlow(false)
    private val _formTouched = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()
    val isNewPlan = _isNewPlan.asStateFlow()

    val saveEnabled = _saveEnabled.asStateFlow()
    val formTouched = _formTouched.asStateFlow()

    fun updatePlanName(value: String) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(name = value))
        _saveEnabled.tryEmit(value.isNotEmpty())
        _formTouched.tryEmit(true)
    }

    fun updatePlanType(value: LessonPlanType) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(type = value))
        _formTouched.tryEmit(true)
    }

    fun updateGradingSystem(value: GradingSystem) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(gradingSystem = value))
        _formTouched.tryEmit(true)
    }

    fun initializePlan(lessonPlanId: Long?) {
        _screenState.tryEmit(DetailsScreenState.Loading)

        lessonPlanId?.let {
            _isNewPlan.tryEmit(false)
            viewModelScope.launch(Dispatchers.IO) {
                lessonPlanRepository.getById(lessonPlanId).let {
                    _lessonPlan.emit(it)
                    _saveEnabled.tryEmit(it.name.isNotEmpty())
                }
            }.invokeOnCompletion {
                _screenState.tryEmit(DetailsScreenState.Edit)
            }
        } ?: run {
            _isNewPlan.tryEmit(true)
            _lessonPlan.tryEmit(LessonPlanModel(isActive = true))
            _screenState.tryEmit(DetailsScreenState.Edit)
        }
    }

    fun savePlan() {
        _screenState.tryEmit(DetailsScreenState.Saving)

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)

            _lessonPlan.value?.let { lessonPlan ->
                lessonPlan.id?.let {
                    lessonPlanRepository.update(lessonPlan)
                } ?: run {
                    val planId = lessonPlanRepository.insert(lessonPlan.apply { isActive = true })
                    lessonPlanRepository.makeOtherPlansNotActive(planId)
                }
            }
        }.invokeOnCompletion {
            _screenState.tryEmit(DetailsScreenState.Finished)
        }
    }
}
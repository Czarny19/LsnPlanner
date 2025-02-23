package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.GradingSystem
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlanViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)
    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)

    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: LessonPlanModel

    val screenState = _screenState.asStateFlow()
    val lessonPlan = _lessonPlan.asStateFlow()

    val saveEnabled = _saveEnabled.asStateFlow()

    var isNewPlan = false
        private set

    val dataChanged = { _lessonPlan.value != _initialData }

    fun updatePlanName(value: String) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(name = value))
        _saveEnabled.tryEmit(value.isNotEmpty())
    }

    fun updatePlanType(value: LessonPlanType) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(type = value))
    }

    fun updateGradingSystem(value: GradingSystem) {
        _lessonPlan.tryEmit(_lessonPlan.value?.copy(gradingSystem = value))
    }

    fun initializePlan(lessonPlanId: Long?) {
        if (_lessonPlan.value != null) {
            return
        }

        _screenState.tryEmit(DetailsScreenState.Loading)

        lessonPlanId?.let { id ->
            isNewPlan = false

            viewModelScope.launch(ioDispatcher) {
                lessonPlanRepository.getById(id).let {
                    _lessonPlan.emit(it)
                    _saveEnabled.tryEmit(it.name.isNotEmpty())
                    _initialData = it.copy()
                }
            }.invokeOnCompletion {
                _screenState.tryEmit(DetailsScreenState.Edit)
            }
        } ?: run {
            isNewPlan = true

            LessonPlanModel(isActive = true).let {
                _lessonPlan.tryEmit(it)
                _initialData = it.copy()
            }

            _screenState.tryEmit(DetailsScreenState.Edit)
        }
    }

    fun savePlan() {
        _screenState.tryEmit(DetailsScreenState.Saving)

        viewModelScope.launch(ioDispatcher) {
            delay(500)

            _lessonPlan.value?.let { lessonPlan ->
                lessonPlan.id?.let {
                    lessonPlanRepository.update(lessonPlan)
                } ?: run {
                    lessonPlanRepository.insert(lessonPlan.apply { isActive = true }).let { id ->
                        lessonPlanRepository.makeOtherPlansNotActive(id)
                    }
                }
            }
        }.invokeOnCompletion {
            _screenState.tryEmit(DetailsScreenState.Finished)
        }
    }
}
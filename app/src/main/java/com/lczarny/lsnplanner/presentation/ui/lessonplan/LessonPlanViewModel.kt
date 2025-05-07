package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.LessonPlanType
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.SessionRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.currentTimestamp
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
    private val sessionRepository: SessionRepository,
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)
    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: LessonPlanModel

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

    fun initializePlan(lessonPlanId: Long?) {
        if (_lessonPlan.value != null) {
            return
        }

        _screenState.update { DetailsScreenState.Loading }

        lessonPlanId?.let { id ->
            viewModelScope.launch(ioDispatcher) {
                lessonPlanRepository.getById(id).let { lessonPlan ->
                    _lessonPlan.update { lessonPlan }
                    _saveEnabled.update { lessonPlan.name.isNotEmpty() }
                    _initialData = lessonPlan.copy()
                }
            }.invokeOnCompletion {
                _screenState.update { DetailsScreenState.Edit }
            }
        } ?: run {
            LessonPlanModel(isActive = true, profileId = sessionRepository.activeProfile.id, createDate = currentTimestamp()).let { lessonPlan ->
                _initialData = lessonPlan.copy()
                _lessonPlan.update { lessonPlan }
                _screenState.update { DetailsScreenState.Create }
            }
        }
    }

    fun savePlan() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            _lessonPlan.value?.let { lessonPlan ->
                lessonPlan.id?.let {
                    lessonPlanRepository.update(lessonPlan)
                    updateOtherPlans(lessonPlan, lessonPlan.isActive)
                } ?: run {
                    val newId = lessonPlanRepository.insert(lessonPlan.apply { isActive = true })
                    updateOtherPlans(lessonPlanRepository.getById(newId), lessonPlan.isActive)
                }
            }
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    private suspend fun updateOtherPlans(lessonPlan: LessonPlanModel, isActive: Boolean) {
        if (isActive) {
            lessonPlanRepository.makeOtherPlansNotActive(lessonPlan)
        }
    }

    private fun checkDataChanged() {
        _dataChanged.update { _initialData != lessonPlan.value }
    }
}
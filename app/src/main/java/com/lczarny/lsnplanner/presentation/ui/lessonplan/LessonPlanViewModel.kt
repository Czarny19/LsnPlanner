package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
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
        setDataChanged()
    }

    fun updatePlanType(value: LessonPlanType) {
        _lessonPlan.update { _lessonPlan.value?.copy(type = value) }
        setDataChanged()
    }

    fun updateAddressEnabled(value: Boolean) {
        _lessonPlan.update { _lessonPlan.value?.copy(addressEnabled = value) }
        setDataChanged()
    }

    fun setPlanAsActive() {
        _lessonPlan.update { _lessonPlan.value?.copy(isActive = true) }
        setDataChanged()
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
            LessonPlanModel(isActive = true, createDate = currentTimestamp()).let { lessonPlan ->
                _lessonPlan.update { lessonPlan }
                _initialData = lessonPlan.copy()
            }

            _screenState.update { DetailsScreenState.Create }
        }
    }

    fun savePlan() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            _lessonPlan.value?.let { lessonPlan ->
                lessonPlan.id?.let {
                    lessonPlanRepository.update(lessonPlan)
                    updateOtherPlans(lessonPlan)
                } ?: run {
                    lessonPlanRepository.insert(lessonPlan.apply { isActive = true }).run {
                        updateOtherPlans(lessonPlan)
                    }
                }
            }
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    private suspend fun updateOtherPlans(lessonPlan: LessonPlanModel) {
        if (lessonPlan.isActive) {
            lessonPlanRepository.makeOtherPlansNotActive(lessonPlan.id!!)
        }
    }

    private fun setDataChanged() {
        _dataChanged.update { true }
    }
}
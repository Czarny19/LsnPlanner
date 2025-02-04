package com.lczarny.lsnplanner.presentation.ui.planclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.data.local.model.PlanClassType
import com.lczarny.lsnplanner.data.local.model.defaultClassType
import com.lczarny.lsnplanner.data.local.repository.PlanClassRepository
import com.lczarny.lsnplanner.presentation.ui.planclass.model.PlanClassState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanClassViewModel @Inject constructor(
    private val planClassRepository: PlanClassRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(PlanClassState.Loading)
    private val _planClassData = MutableStateFlow<PlanClassModel?>(null)
    private val _lessonPlanType = MutableStateFlow(LessonPlanType.School)
    private val _isEdit = MutableStateFlow(false)
    private val _isCyclical = MutableStateFlow(true)

    private var _defaultWeekDay = 0

    private val _planNameError = MutableStateFlow(false)
    private val _planStartDateError = MutableStateFlow(false)
    private val _planClassroomError = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val planClassData = _planClassData.asStateFlow()
    val lessonPlanType = _lessonPlanType.asStateFlow()
    val isEdit = _isEdit.asStateFlow()
    val isCyclical = _isCyclical.asStateFlow()

    val planNameError = _planNameError.asStateFlow()
    val planStartDateError = _planStartDateError.asStateFlow()
    val planClassroomError = _planClassroomError.asStateFlow()

    fun updateName(value: String) {
        _planNameError.update { false }
        _planClassData.update { _planClassData.value?.copy(name = value) }
    }

    fun updateClassType(value: PlanClassType) {
        _planClassData.update { _planClassData.value?.copy(type = value) }
    }

    fun updateClassColor(value: Long) {
        _planClassData.update { _planClassData.value?.copy(color = value) }
    }

    fun updateNote(value: String) {
        _planClassData.update { _planClassData.value?.copy(note = value) }
    }

    fun updateIsCyclical(value: Boolean) {
        _planClassData.update { _planClassData.value?.copy(weekDay = if (value) _defaultWeekDay else null) }
        _planClassData.update { _planClassData.value?.copy(startHour = 0, startMinute = 0) }

        _planStartDateError.update { false }
        _planClassData.update { _planClassData.value?.copy(startDate = null) }

        _isCyclical.update { value }
    }

    fun updateWeekDay(value: Int?) {
        _planClassData.update { _planClassData.value?.copy(weekDay = value) }
    }

    fun updateStartDate(value: Long?) {
        _planStartDateError.update { false }
        _planClassData.update { _planClassData.value?.copy(startDate = value) }
    }

    fun updateStartTime(hour: Int, minute: Int) {
        _planClassData.update { _planClassData.value?.copy(startHour = hour, startMinute = minute) }
    }

    fun updateDuration(value: Int) {
        _planClassData.update { _planClassData.value?.copy(durationMinutes = value) }
    }

    fun updateClassroom(value: String) {
        _planClassroomError.update { false }
        _planClassData.update { _planClassData.value?.copy(classroom = value) }
    }

    fun initializePlanClass(lessonPlanId: Long, lessonPlanType: LessonPlanType, defaultWeekDay: Int, planClassId: Long?) {
        _defaultWeekDay = defaultWeekDay
        _lessonPlanType.update { lessonPlanType }

        if (planClassId != null) {
            _isEdit.update { true }

            viewModelScope.launch(Dispatchers.IO) {
                planClassRepository.classWithToDos(planClassId).flowOn(Dispatchers.IO).collect { planClassWithToDos ->
                    _planClassData.update { planClassWithToDos.planClass }
                }
            }
        } else {
            _isEdit.update { false }
            _planClassData.update {
                PlanClassModel(
                    type = lessonPlanType.defaultClassType(),
                    weekDay = defaultWeekDay,
                    lessonPlanId = lessonPlanId
                )
            }
        }

        _screenState.update { PlanClassState.Edit }
    }

    fun savePlanClass() {
        _planClassData.value?.let {
            var error = false

            if (it.name.isEmpty()) {
                error = true
                _planNameError.update { true }
            }

            if (it.classroom == null || it.classroom!!.isEmpty()) {
                error = true
                _planClassroomError.update { true }
            }

            if (_isCyclical.value.not() && it.startDate == null) {
                error = true
                _planStartDateError.update { true }
            }

            if (error) {
                return
            }

            _screenState.update { PlanClassState.Saving }

            viewModelScope.launch(Dispatchers.IO) {
                if (it.id != null) {
                    planClassRepository.update(it)
                } else {
                    planClassRepository.insert(it)
                }
            }.invokeOnCompletion {
                _screenState.update { PlanClassState.Finished }
            }
        }
    }
}
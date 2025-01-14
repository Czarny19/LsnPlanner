package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.LessonPlanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LessonPlanViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<LessonPlanState>(LessonPlanState.Loading)
    private val _lessonPlanData = MutableStateFlow<LessonPlanModel?>(null)

    private val _planNameError = MutableStateFlow(false)

    private val _planIsDefault = MutableStateFlow(false)
    private val _planIsDefaultEnabled = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val lessonPlanData = _lessonPlanData.asStateFlow()
    val planNameError = _planNameError.asStateFlow()
    val planIsDefaultEnabled = _planIsDefaultEnabled.asStateFlow()

    fun updatePlanName(value: String) {
        _lessonPlanData.update { _lessonPlanData.value?.copy(name = value) }
        _planNameError.update { false }
    }

    fun updatePlanType(value: LessonPlanType) {
        _lessonPlanData.update { _lessonPlanData.value?.copy(type = value) }
    }

    fun updatePlanIsDefault(value: Boolean) {
        _lessonPlanData.update { _lessonPlanData.value?.copy(isDefault = value) }
    }

    fun initializePlan(firstLaunch: Boolean, lessonPlanId: Long?) {
        if (lessonPlanId == null) {
            _planIsDefault.update { firstLaunch }
            _planIsDefaultEnabled.update { firstLaunch.not() }
            _lessonPlanData.update {
                LessonPlanModel(
                    name = "",
                    type = LessonPlanType.School,
                    isDefault = firstLaunch,
                    createDate = Calendar.getInstance().timeInMillis
                )
            }

            _screenState.update { LessonPlanState.Edit }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                lessonPlanRepository.lessonPlan(lessonPlanId).flowOn(Dispatchers.IO).collect { plan ->
                    _lessonPlanData.update { plan }
                    _planIsDefaultEnabled.update { true }
                }
            }.invokeOnCompletion {
                _screenState.update { LessonPlanState.Edit }
            }
        }
    }

    fun savePlan() {
        _lessonPlanData.value?.let {
            if (it.name.isEmpty()) {
                _planNameError.update { true }
                return
            }

            _screenState.update { LessonPlanState.Saving }

            viewModelScope.launch(Dispatchers.IO) {
                if (it.id != null) {
//                    TODO test
//                    lessonPlanRepository.update(it)
                } else {
                    lessonPlanRepository.insert(it)
                }
            }.invokeOnCompletion {
                _screenState.update { LessonPlanState.Finished }
            }
        }
    }
}
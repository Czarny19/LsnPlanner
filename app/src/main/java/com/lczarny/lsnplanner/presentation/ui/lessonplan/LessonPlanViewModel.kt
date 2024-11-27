package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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

    val screenState = _screenState.asStateFlow()

    var lessonPlanId = mutableLongStateOf(-1L)
        private set

    var planName = mutableStateOf("")
        private set

    var planNameError = mutableStateOf(false)
        private set

    var planType = mutableStateOf(LessonPlanType.School)
        private set

    var planIsDefault = mutableStateOf(false)
        private set

    var planIsDefaultEnabled = mutableStateOf(false)
        private set

    fun updatePlanName(value: String) {
        planName.value = value
        planNameError.value = false
    }

    fun updatePlanType(value: LessonPlanType) {
        planType.value = value
    }

    fun updatePlanIsDefault(value: Boolean) {
        planIsDefault.value = value
    }

    fun initializePlan(firstLaunch: Boolean, lessonPlanId: Long?) {
        when (lessonPlanId) {
            null -> {
                _screenState.update { LessonPlanState.Edit }
                planIsDefault.value = firstLaunch
                planIsDefaultEnabled.value = firstLaunch.not()
            }

            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    lessonPlanRepository.lessonPlan(lessonPlanId).flowOn(Dispatchers.IO).collect { plan ->
                        this@LessonPlanViewModel.lessonPlanId.longValue = plan.id!!
                        planName.value = plan.name
                        planType.value = plan.type
                        planIsDefault.value = plan.isDefault
                        planIsDefaultEnabled.value = true
                    }
                }.invokeOnCompletion {
                    _screenState.update { LessonPlanState.Edit }
                }
            }
        }
    }

    fun savePlan() {
        if (planName.value.isEmpty()) {
            planNameError.value = true
            return
        }

        _screenState.value = LessonPlanState.Saving

        viewModelScope.launch(Dispatchers.IO) {
            if (lessonPlanId.longValue >= 0) {
                LessonPlanModel(
                    id = lessonPlanId.longValue,
                    name = planName.value,
                    type = planType.value,
                    isDefault = planIsDefault.value,
                    createDate = Calendar.getInstance().timeInMillis
                ).run {
                    // TODO update
                    // TODO is default zdjąć z innych
                }
            } else {
                LessonPlanModel(
                    name = planName.value,
                    type = planType.value,
                    isDefault = planIsDefault.value,
                    createDate = Calendar.getInstance().timeInMillis
                ).run {
                    // TODO is default zdjąć z innych
                    lessonPlanRepository.insert(this)
                }
            }
        }.invokeOnCompletion {
            _screenState.value = LessonPlanState.Finished
        }
    }
}
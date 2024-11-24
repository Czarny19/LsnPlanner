package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.LessonPlanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LessonPlanViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<LessonPlanState>(LessonPlanState.Loading)
    private val _planToEdit = MutableStateFlow<LessonPlan?>(null)

    val screenState = _screenState.asStateFlow()

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

    fun initializePlan(firstLaunch: Boolean, planId: Long?) {
        when (planId) {
            null -> {
                _screenState.update { LessonPlanState.Edit }
                planIsDefault.value = firstLaunch
                planIsDefaultEnabled.value = !firstLaunch
            }

            else -> {
                _screenState.update { LessonPlanState.Edit }
            }
        }
    }

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

    fun savePlan() {
        if (planName.value.isEmpty()) {
            planNameError.value = true
            return
        }

        _screenState.value = LessonPlanState.Saving

        viewModelScope.launch(Dispatchers.IO) {
            val lessonPlan = _planToEdit.value ?: LessonPlan(
                name = planName.value,
                type = planType.value,
                isDefault = planIsDefault.value,
                createDate = Calendar.getInstance().toInstant().toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault())
            )

            lessonPlanRepository.insert(lessonPlan)
        }.invokeOnCompletion {
            _screenState.value = LessonPlanState.Finished
        }
    }
}
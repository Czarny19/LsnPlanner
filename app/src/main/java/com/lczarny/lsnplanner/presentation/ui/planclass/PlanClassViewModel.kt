package com.lczarny.lsnplanner.presentation.ui.planclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.data.local.repository.PlanClassRepository
import com.lczarny.lsnplanner.presentation.ui.planclass.model.PlanClassState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanClassViewModel @Inject constructor(
    private val planClassRepository: PlanClassRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<PlanClassState>(PlanClassState.Loading)
    private val _lessonPlanId = MutableStateFlow<Long>(-1L)
    private val _planClassData = MutableStateFlow<PlanClassModel?>(null)

    val screenState = _screenState.asStateFlow()
    val lessonPlanId = _lessonPlanId.asStateFlow()
    val planClassData = _planClassData.asStateFlow()

    fun intializePlanClass(lessonPlanId: Long, planClass: PlanClassModel?) {
        _lessonPlanId.update { lessonPlanId }

        if (planClass != null) {
            _planClassData.update { planClass }
        } else {
            _planClassData.update {
                PlanClassModel(
                    name = "",
                    isCyclical = true,
                    lessonPlanId = lessonPlanId
                )
            }
        }
    }

    fun savePlanClass() {
        _screenState.update { PlanClassState.Saving }

        viewModelScope.launch(Dispatchers.IO) {
            _planClassData.value?.let {
                if (it.id != null) {
                    planClassRepository.update(it)
                } else {
                    planClassRepository.insert(it)
                }
            }
        }.invokeOnCompletion {
            _screenState.update { PlanClassState.Finished }
        }
    }
}
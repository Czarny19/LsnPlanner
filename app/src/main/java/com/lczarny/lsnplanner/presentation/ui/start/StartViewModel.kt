package com.lczarny.lsnplanner.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.presentation.ui.start.model.StartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(StartState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        checkIfDefaultPlanExists()
    }

    private fun checkIfDefaultPlanExists() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(1000)

            lessonPlanRepository.checkIfDefaultPlanExists().flowOn(Dispatchers.IO).collect { exists ->
                when (exists) {
                    true -> _screenState.update { StartState.StartApp }
                    false -> _screenState.update { StartState.FirstLaunch }
                }
            }
        }
    }
}
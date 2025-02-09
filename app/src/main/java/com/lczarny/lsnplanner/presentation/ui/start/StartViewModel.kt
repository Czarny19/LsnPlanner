package com.lczarny.lsnplanner.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val lessonPlanRepository: LessonPlanRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(StartScreenState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        checkIfActivePlanExists()
    }

    private fun checkIfActivePlanExists() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)

            lessonPlanRepository.checkIfActivePlanExists().let {
                when (it) {
                    true -> _screenState.emit(StartScreenState.StartApp)
                    false -> _screenState.emit(StartScreenState.FirstLaunch)
                }
            }
        }
    }
}
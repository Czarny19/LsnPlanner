package com.lczarny.lsnplanner.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.SettingRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.StartScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lessonPlanRepository: LessonPlanRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(StartScreenState.Loading)
    private val _startEnabled = MutableStateFlow(false)

    private var _userName = ""

    val screenState = _screenState.asStateFlow()
    val startEnabled = _startEnabled.asStateFlow()

    init {
        checkActivePlan()
    }

    fun updateUserName(userName: String) {
        _userName = userName
        _startEnabled.update { userName.isNotEmpty() }
    }

    fun save() {
        viewModelScope.launch(ioDispatcher) {
            settingRepository.setUserName(_userName)
        }.invokeOnCompletion {
            _screenState.update { StartScreenState.UserNameSaved }
        }
    }

    private fun checkActivePlan() {
        viewModelScope.launch(ioDispatcher) {
            if (lessonPlanRepository.checkIfActivePlanExists()) {
                _screenState.update { StartScreenState.StartApp }
            } else {
                _screenState.update { StartScreenState.FirstLaunch }
            }
        }
    }
}
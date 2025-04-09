package com.lczarny.lsnplanner.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.StartScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lessonPlanRepository: LessonPlanRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(StartScreenState.Loading)
    private val _startEnabled = MutableStateFlow(false)

    private var _userName = ""

    val screenState = _screenState.asStateFlow()
    val startEnabled = _startEnabled.asStateFlow()

    init {
        loadAppSettingsAndActivePlan()
    }

    fun updateUserName(userName: String) {
        _userName = userName
        _startEnabled.update { userName.isNotEmpty() }
    }

    fun save() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.setUserName(_userName)
        }.invokeOnCompletion {
            _screenState.update { StartScreenState.UserNameSaved }
        }
    }

    private fun loadAppSettingsAndActivePlan() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppSettings().flowOn(ioDispatcher).collect { appSettings ->
                if (appSettings.userName == null || lessonPlanRepository.checkIfActivePlanExists().not()) {
                    _screenState.update { StartScreenState.FirstLaunch }
                } else {
                    _screenState.update { StartScreenState.StartApp }
                }
            }
        }
    }
}
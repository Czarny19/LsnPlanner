package com.lczarny.lsnplanner.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.repository.AuthError
import com.lczarny.lsnplanner.data.common.repository.AuthRepository
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.ProfileRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val lessonPlanRepository: LessonPlanRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(LoginScreenState.Loading)
    private val _signInEnabled = MutableStateFlow(false)
    private val _resetPasswordEnabled = MutableStateFlow(false)

    private var _email = ""
    private var _password = ""

    val screenState = _screenState.asStateFlow()
    val signInEnabled = _signInEnabled.asStateFlow()
    val resetPasswordEnabled = _resetPasswordEnabled.asStateFlow()

    init {
        loadUserInfo()
    }

    fun updateEmail(email: String) {
        _email = email
        _signInEnabled.update { _email.isNotEmpty() && _password.isNotEmpty() }
        _resetPasswordEnabled.update { email.isNotEmpty() }
    }

    fun updatePassword(password: String) {
        _password = password
        _signInEnabled.update { _email.isNotEmpty() && _password.isNotEmpty() }
    }

    fun signIn(onFinished: () -> Unit, onError: (AuthError) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            authRepository.signIn(_email, _password)?.let { error ->
                onError.invoke(error)
            } ?: run {
                onFinished.invoke()
            }
        }
    }

    private fun loadUserInfo() {
        viewModelScope.launch(ioDispatcher) {
            authRepository.getSessionStatus().collect { status ->
                if (status is SessionStatus.Authenticated && status.session.user != null) {
                    profileRepository.getByEmail(status.session.user!!.email!!).let { profile ->

                    }


                } else {
                    _screenState.update { LoginScreenState.SignIn }
                }
            }
        }


//        viewModelScope.launch(ioDispatcher) {
//            dataStoreRepository.getAppSettings().flowOn(ioDispatcher).collect { appSettings ->
//                if (appSettings.userName == null || lessonPlanRepository.checkIfActivePlanExists().not()) {
//                    _screenState.update { LoginScreenState.FirstLaunch }
//                } else {
//                    _screenState.update { LoginScreenState.StartApp }
//                }
//            }
//        }
    }
}
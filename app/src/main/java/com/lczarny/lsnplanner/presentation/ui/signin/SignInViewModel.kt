package com.lczarny.lsnplanner.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.toProfile
import com.lczarny.lsnplanner.data.common.repository.AuthError
import com.lczarny.lsnplanner.data.common.repository.AuthRepository
import com.lczarny.lsnplanner.data.common.repository.ProfileRepository
import com.lczarny.lsnplanner.data.common.repository.SessionRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.SignInScreenState
import com.lczarny.lsnplanner.utils.isValidEmail
import com.lczarny.lsnplanner.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow(SignInScreenState.Loading)

    private val _formEnabled = MutableStateFlow(true)
    private val _signInEnabled = MutableStateFlow(false)
    private val _resetPasswordEnabled = MutableStateFlow(false)

    private val _emailInvalid = MutableStateFlow(false)
    private val _passwordInvalid = MutableStateFlow(false)

    private var _email = ""
    private var _password = ""

    val screenState = _screenState.asStateFlow()

    val formEnabled = _formEnabled.asStateFlow()
    val signInEnabled = _signInEnabled.asStateFlow()
    val resetPasswordEnabled = _resetPasswordEnabled.asStateFlow()

    val emailInvalid = _emailInvalid.asStateFlow()
    val passwordInvalid = _passwordInvalid.asStateFlow()

    init {
        watchUserInfo()
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

    fun signIn(onError: (AuthError) -> Unit) {
        _formEnabled.update { false }

        viewModelScope.launch(ioDispatcher) {
            authRepository.signIn(_email, _password)?.let { error ->
                _formEnabled.update { true }
                onError.invoke(error)
            }
        }
    }

    fun signUp(onError: (AuthError) -> Unit) {
        _formEnabled.update { false }

        _emailInvalid.update { _email.isValidEmail().not() }
        _passwordInvalid.update { _password.isValidPassword().not() }

        if (_emailInvalid.value || _passwordInvalid.value) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            authRepository.signUp(_email, _password)?.let { error ->
                _formEnabled.update { true }
                onError.invoke(error)
            }
        }
    }

    fun resetPassword(onFinished: () -> Unit, onError: (AuthError) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            authRepository.resetPassword(_email)
                ?.let { error -> onError.invoke(error) }
                ?: run { onFinished.invoke() }
        }
    }

    private fun watchUserInfo() {
        viewModelScope.launch(ioDispatcher) {
            sessionRepository.status.collect { status ->
                val currentUser = sessionRepository.user()

                if (status is SessionStatus.Authenticated && currentUser != null) {
                    if (profileRepository.loadActiveProfile(currentUser.email!!)) {
                        _screenState.update { SignInScreenState.Done }
                    } else {
                        createNewProfile(currentUser)
                    }
                } else {
                    _screenState.update { SignInScreenState.SignIn }
                }
            }
        }
    }

    private fun createNewProfile(user: UserInfo) {
        viewModelScope.launch(ioDispatcher) {
            profileRepository.insert(user.toProfile()).let { newProfileId ->
                profileRepository.loadActiveProfile(user.email!!).run {
                    _screenState.update { SignInScreenState.Done }
                }
            }
        }
    }
}
package com.lczarny.lsnplanner.presentation.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.auth.LoadUserProfileUseCase
import com.lczarny.lsnplanner.domain.auth.ResetPasswordUseCase
import com.lczarny.lsnplanner.domain.auth.SignInUseCase
import com.lczarny.lsnplanner.domain.auth.SignUpUseCase
import com.lczarny.lsnplanner.model.SignInScreenState
import com.lczarny.lsnplanner.online.model.AuthError
import com.lczarny.lsnplanner.utils.isValidEmail
import com.lczarny.lsnplanner.utils.isValidPassword
import com.lczarny.lsnplanner.utils.updateIfChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val loadUserProfileUseCase: LoadUserProfileUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
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
        _signInEnabled.updateIfChanged(_email.isNotEmpty() && _password.isNotEmpty())
        _resetPasswordEnabled.updateIfChanged(email.isNotEmpty())
    }

    fun updatePassword(password: String) {
        _password = password
        _signInEnabled.updateIfChanged(_email.isNotEmpty() && _password.isNotEmpty())
    }

    fun signIn(onError: (AuthError) -> Unit) {
        _formEnabled.update { false }

        viewModelScope.launch(ioDispatcher) {
            signInUseCase.invoke(_email, _password)?.let { error ->
                _formEnabled.update { true }
                onError.invoke(error)
            }
        }
    }

    fun signUp(onError: (AuthError) -> Unit) {
        _formEnabled.update { false }

        _emailInvalid.updateIfChanged(_email.isValidEmail().not())
        _passwordInvalid.updateIfChanged(_password.isValidPassword().not())

        if (_emailInvalid.value || _passwordInvalid.value) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            signUpUseCase.invoke(_email, _password)?.let { error ->
                _formEnabled.update { true }
                onError.invoke(error)
            }
        }
    }

    fun resetPassword(onFinished: () -> Unit, onError: (AuthError) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            resetPasswordUseCase.invoke(_email)
                ?.let { error -> onError.invoke(error) }
                ?: run { onFinished.invoke() }
        }
    }

    private fun watchUserInfo() {
        viewModelScope.launch(ioDispatcher) {
            loadUserProfileUseCase.invoke(
                onSignIn = { _screenState.update { SignInScreenState.Done } },
                onNoSession = { _screenState.update { SignInScreenState.SignIn } }
            )
        }
    }
}
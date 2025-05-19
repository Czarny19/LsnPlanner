package com.lczarny.lsnplanner.presentation.ui.signin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.online.model.AuthError
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.PrimaryButtonVariant
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.signin.SignInViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun SignInForm(viewModel: SignInViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val formEnabled by viewModel.formEnabled.collectAsStateWithLifecycle()
    val signInEnabled by viewModel.signInEnabled.collectAsStateWithLifecycle()
    val resetPasswordEnabled by viewModel.resetPasswordEnabled.collectAsStateWithLifecycle()

    val emailInvalid by viewModel.emailInvalid.collectAsStateWithLifecycle()
    val passwordInvalid by viewModel.passwordInvalid.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<SignInScreenSnackbar>(Channel.CONFLATED) }
    val errorSnackbarChannel = remember { Channel<AuthError>(Channel.CONFLATED) }

    SignInSnackBar(snackbarHostState, snackbarChannel, errorSnackbarChannel)

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppPadding.MD_PADDING)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
        ) {
            SignInWelcome()

            OutlinedInputField(
                label = stringResource(R.string.user_email),
                onValueChange = { email -> viewModel.updateEmail(email) },
                readOnly = formEnabled.not(),
                error = if (emailInvalid) InputError.CustomErrorMsg(stringResource(R.string.error_email_invalid)) else null
            )

            OutlinedInputField(
                label = stringResource(R.string.user_password),
                onValueChange = { password -> viewModel.updatePassword(password) },
                readOnly = formEnabled.not(),
                obscured = true,
                error = if (passwordInvalid) InputError.CustomErrorMsg(stringResource(R.string.error_password_hint)) else null,
                supportingText = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(R.string.signin_reset_password),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable(enabled = resetPasswordEnabled) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()

                                    viewModel.resetPassword(
                                        onFinished = { snackbarChannel.trySend(SignInScreenSnackbar.PasswordResetSent) },
                                        onError = { error -> errorSnackbarChannel.trySend(error) }
                                    )
                                }
                                .padding(AppPadding.XSM_PADDING),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surfaceTint)
                        )
                    }
                }
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppPadding.MD_PADDING),
                text = stringResource(R.string.signin_do_signin),
                enabled = signInEnabled && formEnabled,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    viewModel.signIn(onError = { error -> errorSnackbarChannel.trySend(error) })
                }
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.signin_do_signup),
                variant = PrimaryButtonVariant.Alt,
                enabled = signInEnabled && formEnabled,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    viewModel.signUp(onError = { error -> errorSnackbarChannel.trySend(error) })
                }
            )
        }
    }
}

@Composable
fun SignInWelcome() {
    Card(modifier = Modifier.padding(bottom = AppPadding.LG_PADDING)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = AppPadding.LG_PADDING, horizontal = AppPadding.MD_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING)
        ) {
            Icon(
                AppIcons.WELCOME,
                modifier = Modifier
                    .padding(bottom = AppPadding.MD_PADDING)
                    .size(AppSizes.XL_ICON),
                contentDescription = stringResource(R.string.signin_welcome),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                stringResource(R.string.signin_welcome),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            Text(
                stringResource(R.string.signin_info),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}
package com.lczarny.lsnplanner.presentation.ui.signin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.repository.AuthError
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.signin.SignInViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun SignInForm(viewModel: SignInViewModel) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val signInEnabled by viewModel.signInEnabled.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<AuthError>(Channel.CONFLATED) }

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                AuthError.InvalidCredentials -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_auth_invalid_credentials),
                    withDismissAction = true
                )

                AuthError.UserBanned -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_auth_banned),
                    withDismissAction = true
                )

                AuthError.UserAlreadyExists -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_auth_user_exists),
                    withDismissAction = true
                )

                AuthError.Other -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_auth_unknown),
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppPadding.SCREEN_PADDING)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
        ) {
            Icon(
                AppIcons.WELCOME,
                modifier = Modifier
                    .padding(top = AppPadding.LG_PADDING)
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
                modifier = Modifier.padding(bottom = AppPadding.LG_PADDING),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            OutlinedInputField(
                label = stringResource(R.string.email),
                onValueChange = { email -> viewModel.updateEmail(email) }
            )

            OutlinedInputField(
                label = stringResource(R.string.password),
                onValueChange = { password -> viewModel.updatePassword(password) }
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.signin_do_signin),
                enabled = signInEnabled,
                onClick = {
                    keyboardController?.hide()
                    viewModel.signIn(onFinished = {

                    }, onError = { error -> snackbarChannel.trySend(error) })
                }
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.signin_do_signup),
                enabled = signInEnabled,
                onClick = {
                    keyboardController?.hide()
                    viewModel.signIn(onFinished = {

                    }, onError = { error -> snackbarChannel.trySend(error) })
                }
            )
        }
    }
}
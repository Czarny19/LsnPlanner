package com.lczarny.lsnplanner.presentation.ui.signin.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.online.model.AuthError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class SignInScreenSnackbar {
    PasswordResetSent
}

@Composable
fun SignInSnackBar(snackbarHostState: SnackbarHostState, snackbarChannel: Channel<SignInScreenSnackbar>, errorSnackbarChannel: Channel<AuthError>) {
    val context = LocalContext.current

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            if (it == SignInScreenSnackbar.PasswordResetSent) {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_password_reset_sent),
                    withDismissAction = true
                )
            }
        }
    }

    LaunchedEffect(errorSnackbarChannel) {
        errorSnackbarChannel.receiveAsFlow().collect {
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
}
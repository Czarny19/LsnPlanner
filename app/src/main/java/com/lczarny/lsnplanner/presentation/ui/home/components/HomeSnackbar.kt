package com.lczarny.lsnplanner.presentation.ui.home.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.lczarny.lsnplanner.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class HomeScreenSnackbar {
    ResetTutorials
}

@Composable
fun HomeSnackbar(snackbarHostState: SnackbarHostState, snackbarChannel: Channel<HomeScreenSnackbar>) {
    val context = LocalContext.current

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                HomeScreenSnackbar.ResetTutorials -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.reset_tutorials_done),
                    withDismissAction = true
                )
            }
        }
    }
}
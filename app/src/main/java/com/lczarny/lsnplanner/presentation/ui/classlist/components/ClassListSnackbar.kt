package com.lczarny.lsnplanner.presentation.ui.classlist.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.ui.classlist.ClassListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class ClassListScreenSnackbar {
    Deleted
}

@Composable
fun ClassListSnackbar(snackbarHostState: SnackbarHostState, snackbarChannel: Channel<ClassListScreenSnackbar>, viewModel: ClassListViewModel) {
    val context = LocalContext.current

    val selectedClassName by viewModel.selectedClassName.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                ClassListScreenSnackbar.Deleted -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_class_deleted, selectedClassName),
                    withDismissAction = true
                )
            }
        }
    }
}
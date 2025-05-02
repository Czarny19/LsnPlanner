package com.lczarny.lsnplanner.presentation.ui.lessonplanlist.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.LessonPlanListViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class ListPickerScreenSnackbar {
    SetActive,
    Deleted
}

@Composable
fun LessonPlanListSnackbar(
    snackbarHostState: SnackbarHostState,
    snackbarChannel: Channel<ListPickerScreenSnackbar>,
    viewModel: LessonPlanListViewModel
) {
    val context = LocalContext.current

    val selectedPlanName by viewModel.selectedPlanName.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                ListPickerScreenSnackbar.SetActive -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_plan_set_active, selectedPlanName),
                    withDismissAction = true
                )

                ListPickerScreenSnackbar.Deleted -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_plan_deleted, selectedPlanName),
                    withDismissAction = true
                )
            }
        }
    }
}
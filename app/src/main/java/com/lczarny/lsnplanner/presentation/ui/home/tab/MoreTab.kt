package com.lczarny.lsnplanner.presentation.ui.home.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.ConfirmationDialog
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.home.HomeScreenSnackbar
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun MoreTab(padding: PaddingValues, viewModel: HomeViewModel, snackbarChannel: Channel<HomeScreenSnackbar>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        MoreTabButton(
            viewModel = viewModel,
            icon = Icons.Filled.SettingsBackupRestore,
            label = stringResource(R.string.reset_tutorials),
            dialogMsg = stringResource(R.string.reset_tutorials_msg),
            onClick = { viewModel.resetTutorials { snackbarChannel.trySend(HomeScreenSnackbar.ResetTutorials) } }
        )
        MoreTabButton(
            viewModel = viewModel,
            icon = Icons.Filled.DeleteForever,
            label = stringResource(R.string.todo_delete_all_historical),
            dialogMsg = stringResource(R.string.todo_delete_all_historical_msg),
            onClick = { viewModel.deleteAllHistoricalToDos { snackbarChannel.trySend(HomeScreenSnackbar.DeleteHistoricalToDos) } }
        )
    }
}

@Composable
fun MoreTabButton(viewModel: HomeViewModel, icon: ImageVector, label: String, dialogMsg: String, onClick: () -> Unit) {
    var confirmationDialogOpen by remember { mutableStateOf(false) }
    val confirmDialogState by viewModel.confirmDialogState.collectAsState()

    ConfirmationDialog(confirmationDialogOpen, confirmDialogState)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                confirmationDialogOpen = true

                viewModel.setConfirmationDialogState(
                    title = label,
                    text = dialogMsg,
                    onConfirm = {
                        confirmationDialogOpen = false
                        onClick.invoke()
                    },
                    onDismiss = { confirmationDialogOpen = false }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppPadding.MD_PADDING),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(AppSizes.MD_ICON),
                imageVector = icon,
                contentDescription = label
            )
            Text(
                label,
                modifier = Modifier.padding(start = AppPadding.SM_PADDING),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
    HorizontalDivider()
}
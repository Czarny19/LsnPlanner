package com.lczarny.lsnplanner.presentation.ui.home.more

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
    var confirmationDialogOpen by remember { mutableStateOf(false) }
    val confirmDialogState by viewModel.confirmDialogState.collectAsState()

    val deleteHistToDosLabel = stringResource(R.string.todo_delete_all_historical)
    val deleteHistToDosMsg = stringResource(R.string.todo_delete_all_historical_msg)

    ConfirmationDialog(confirmationDialogOpen, confirmDialogState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        MoreTabButton(
            icon = Icons.Filled.DeleteForever,
            label = deleteHistToDosLabel,
            onClick = {
                confirmationDialogOpen = true

                viewModel.setConfirmationDialogState(
                    title = deleteHistToDosLabel,
                    text = deleteHistToDosMsg,
                    onConfirm = {
                        confirmationDialogOpen = false
                        viewModel.deleteAllHistoricalToDos {
                            snackbarChannel.trySend(HomeScreenSnackbar.DeleteHistoricalToDos)
                        }
                    },
                    onDismiss = { confirmationDialogOpen = false }
                )
            }
        )
    }
}

@Composable
fun MoreTabButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppPadding.mdPadding),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(AppSizes.mdIcon),
                imageVector = icon,
                contentDescription = label
            )
            Text(
                label,
                modifier = Modifier.padding(start = AppPadding.smPadding),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
    HorizontalDivider()
}
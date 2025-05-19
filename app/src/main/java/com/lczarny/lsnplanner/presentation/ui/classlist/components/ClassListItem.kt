package com.lczarny.lsnplanner.presentation.ui.classlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.presentation.components.BasicDialogState
import com.lczarny.lsnplanner.presentation.components.ColorIndicator
import com.lczarny.lsnplanner.presentation.components.DeleteIcon
import com.lczarny.lsnplanner.presentation.components.DeleteItemDialog
import com.lczarny.lsnplanner.presentation.components.InfoChip
import com.lczarny.lsnplanner.presentation.components.ListItemTitle
import com.lczarny.lsnplanner.presentation.components.OptionsMenuIcon
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.model.mapper.toLabel
import com.lczarny.lsnplanner.model.mapper.toPlanClassTypeIcon
import com.lczarny.lsnplanner.presentation.navigation.ClassDetailsRoute
import com.lczarny.lsnplanner.presentation.ui.classlist.ClassListViewModel
import kotlinx.coroutines.channels.Channel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClassListItem(
    navController: NavController,
    viewModel: ClassListViewModel,
    snackbarChannel: Channel<ClassListScreenSnackbar>,
    classInfo: ClassInfo,
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(ClassDetailsRoute(classInfoId = classInfo.id!!)) }
            .padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.MD_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING)
    ) {
        ColorIndicator(color = Color(classInfo.color))
        ListItemTitle(modifier = Modifier.weight(1.0f), text = classInfo.name)
        InfoChip(label = classInfo.type.toLabel(context), imageVector = classInfo.type.toPlanClassTypeIcon())
        ClassListItemMenu(viewModel, snackbarChannel, classInfo)
    }

    HorizontalDivider()
}

@Composable
fun ClassListItemMenu(
    viewModel: ClassListViewModel,
    snackbarChannel: Channel<ClassListScreenSnackbar>,
    classInfo: ClassInfo,
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var deleteConfirmationDialogOpen by remember { mutableStateOf(false) }

    DeleteItemDialog(
        deleteConfirmationDialogOpen,
        BasicDialogState(
            title = stringResource(R.string.class_delete),
            text = stringResource(R.string.class_delete_question),
            onConfirm = {
                deleteConfirmationDialogOpen = false

                viewModel.deleteClass(classInfo) {
                    snackbarChannel.trySend(ClassListScreenSnackbar.Deleted)
                }
            },
            onDismiss = { deleteConfirmationDialogOpen = false }
        )
    )

    IconButton(onClick = { dropDownExpanded = true }) {
        OptionsMenuIcon()

        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = { dropDownExpanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.class_delete)) },
                leadingIcon = { DeleteIcon(contentDescription = stringResource(R.string.class_delete)) },
                onClick = {
                    dropDownExpanded = false
                    deleteConfirmationDialogOpen = true
                }
            )
        }
    }
}
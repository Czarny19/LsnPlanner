package com.lczarny.lsnplanner.presentation.ui.lessonplanlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.BasicDialogState
import com.lczarny.lsnplanner.presentation.components.DeleteIcon
import com.lczarny.lsnplanner.presentation.components.DeleteItemDialog
import com.lczarny.lsnplanner.presentation.components.ListItemTitle
import com.lczarny.lsnplanner.presentation.components.OptionsMenuIcon
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.LessonPlanListViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun LessonPlanListItem(
    navController: NavController,
    viewModel: LessonPlanListViewModel,
    snackbarChannel: Channel<ListPickerScreenSnackbar>,
    lessonPlan: LessonPlan,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(LessonPlanRoute(lessonPlanId = lessonPlan.id!!)) }
            .padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.MD_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            AppIcons.SELECTED,
            contentDescription = stringResource(R.string.plan_active),
            tint = if (lessonPlan.isActive) MaterialTheme.colorScheme.tertiary else Color.Transparent
        )
        ListItemTitle(modifier = Modifier.padding(horizontal = AppPadding.MD_PADDING), text = lessonPlan.name)
        Spacer(Modifier.weight(1.0f))
        LessonPlanListItemMenu(viewModel, snackbarChannel, lessonPlan)
    }

    HorizontalDivider()
}

@Composable
private fun LessonPlanListItemMenu(
    viewModel: LessonPlanListViewModel,
    snackbarChannel: Channel<ListPickerScreenSnackbar>,
    lessonPlan: LessonPlan,
) {
    if (lessonPlan.isActive) {
        return IconButton(enabled = false, onClick = {}) {}
    }

    var dropDownExpanded by remember { mutableStateOf(false) }
    var deleteConfirmationDialogOpen by remember { mutableStateOf(false) }

    DeleteItemDialog(
        deleteConfirmationDialogOpen,
        BasicDialogState(
            title = stringResource(R.string.plan_delete),
            text = stringResource(R.string.plan_delete_question),
            onConfirm = {
                deleteConfirmationDialogOpen = false

                viewModel.deletePlan(lessonPlan) {
                    snackbarChannel.trySend(ListPickerScreenSnackbar.Deleted)
                }
            },
            onDismiss = { deleteConfirmationDialogOpen = false }
        )
    )

    IconButton(onClick = { dropDownExpanded = true }) {
        OptionsMenuIcon()

        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = { dropDownExpanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.plan_make_active)) },
                leadingIcon = { Icon(AppIcons.SELECTED, contentDescription = stringResource(R.string.plan_make_active)) },
                onClick = {
                    dropDownExpanded = false

                    viewModel.makePlanActive(lessonPlan) {
                        snackbarChannel.trySend(ListPickerScreenSnackbar.SetActive)
                    }
                }
            )

            HorizontalDivider()

            DropdownMenuItem(
                text = { Text(stringResource(R.string.plan_delete)) },
                leadingIcon = { DeleteIcon(contentDescription = stringResource(R.string.plan_delete)) },
                onClick = {
                    dropDownExpanded = false
                    deleteConfirmationDialogOpen = true
                }
            )
        }
    }
}
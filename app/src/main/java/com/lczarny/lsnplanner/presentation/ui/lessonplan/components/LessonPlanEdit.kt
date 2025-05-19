package com.lczarny.lsnplanner.presentation.ui.lessonplan.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DisplayField
import com.lczarny.lsnplanner.presentation.components.InfoCard
import com.lczarny.lsnplanner.presentation.components.OutlinedCheckbox
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.SaveIcon
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanViewModel
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate

@Composable
fun LessonPlanEdit(navController: NavController, viewModel: LessonPlanViewModel) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()

    var discardChangesDialogOpen by remember { mutableStateOf(false) }

    DiscardChangesDialog(
        visible = discardChangesDialogOpen,
        onConfirm = {
            discardChangesDialogOpen = false
            navController.popBackStack()
        },
        onDismiss = { discardChangesDialogOpen = false }
    )

    BackHandler(dataChanged) { discardChangesDialogOpen = true }

    lessonPlan?.let { lessonPlan ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = lessonPlan.name,
                    onNavIconClick = {
                        if (dataChanged) {
                            discardChangesDialogOpen = true
                        } else {
                            navController.popBackStack()
                        }
                    },
                    actions = {
                        if (dataChanged) IconButton(onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.savePlan()
                        }) { SaveIcon() }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(AppPadding.MD_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
            ) {
                DisplayField(
                    label = stringResource(R.string.plan_type),
                    text = lessonPlan.type.getLabel(context),
                    icon = {
                        Icon(
                            AppIcons.PLAN,
                            modifier = Modifier.size(AppSizes.MD_ICON),
                            contentDescription = stringResource(R.string.plan_type)
                        )
                    },
                )

                DisplayField(
                    label = stringResource(R.string.plan_create_date),
                    text = lessonPlan.createDate.convertMillisToSystemDate(context),
                    icon = {
                        Icon(
                            AppIcons.EDIT_DATE,
                            modifier = Modifier.size(AppSizes.MD_ICON),
                            contentDescription = stringResource(R.string.plan_create_date)
                        )
                    },
                )

                DisplayField(
                    text = stringResource(if (lessonPlan.isActive) R.string.plan_is_active else R.string.plan_is_not_active),
                    icon = {
                        if (lessonPlan.isActive) {
                            Icon(
                                AppIcons.ACTIVE,
                                modifier = Modifier.size(AppSizes.MD_ICON),
                                contentDescription = stringResource(R.string.plan_is_active)
                            )
                        } else {
                            Icon(
                                AppIcons.INACTIVE,
                                modifier = Modifier.size(AppSizes.MD_ICON),
                                contentDescription = stringResource(R.string.plan_is_not_active)
                            )
                        }
                    }
                )

                if (lessonPlan.isActive.not()) PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.plan_make_active),
                    onClick = { viewModel.setPlanAsActive() }
                )

                HorizontalDivider()

                InfoCard(text = stringResource(R.string.plan_address_enabled_change_info))

                OutlinedCheckbox(
                    initialValue = lessonPlan.addressEnabled,
                    label = stringResource(R.string.plan_address_enabled),
                    onCheckedChange = { addressEnabled -> viewModel.updateAddressEnabled(addressEnabled) }
                )
            }
        }
    }
}
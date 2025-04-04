package com.lczarny.lsnplanner.presentation.ui.lessonplan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DisplayField
import com.lczarny.lsnplanner.presentation.components.InfoCard
import com.lczarny.lsnplanner.presentation.components.OutlinedCheckbox
import com.lczarny.lsnplanner.presentation.components.PlanActiveIcon
import com.lczarny.lsnplanner.presentation.components.PlanCreateDateIcon
import com.lczarny.lsnplanner.presentation.components.PlanTypeIcon
import com.lczarny.lsnplanner.presentation.components.PredefinedDialogState
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.SaveIcon
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanViewModel
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate

@Composable
fun LessonPlanEdit(navController: NavController, viewModel: LessonPlanViewModel) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()

    var discardChangesDialogOpen by remember { mutableStateOf(false) }

    DiscardChangesDialog(
        discardChangesDialogOpen,
        PredefinedDialogState(
            onConfirm = {
                discardChangesDialogOpen = false
                navController.popBackStack()
            },
            onDismiss = { discardChangesDialogOpen = false }
        )
    )

    lessonPlan?.let { lessonPlanData ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = lessonPlanData.name,
                    navIcon = {
                        AppBarBackIconButton(onClick = {
                            if (dataChanged) {
                                discardChangesDialogOpen = true
                            } else {
                                navController.popBackStack()
                            }
                        })
                    },
                    actions = {
                        if (dataChanged) IconButton(onClick = { viewModel.savePlan() }) { SaveIcon() }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(AppPadding.SCREEN_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                DisplayField(
                    modifier = Modifier.padding(bottom = AppPadding.MD_PADDING),
                    label = stringResource(R.string.plan_type),
                    text = lessonPlanData.type.getLabel(context),
                    icon = { PlanTypeIcon(modifier = Modifier.size(AppSizes.MD_ICON)) },
                )

                DisplayField(
                    modifier = Modifier.padding(bottom = AppPadding.MD_PADDING),
                    label = stringResource(R.string.plan_create_date),
                    text = lessonPlanData.createDate.convertMillisToSystemDate(context),
                    icon = { PlanCreateDateIcon(modifier = Modifier.size(AppSizes.MD_ICON)) },
                )

                DisplayField(
                    modifier = Modifier.padding(bottom = AppPadding.MD_PADDING),
                    text = stringResource(if (lessonPlanData.isActive) R.string.plan_is_active else R.string.plan_is_not_active),
                    icon = { PlanActiveIcon(modifier = Modifier.size(AppSizes.MD_ICON), active = lessonPlanData.isActive) }
                )

                if (lessonPlanData.isActive.not()) PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppPadding.MD_PADDING),
                    text = stringResource(R.string.plan_make_active),
                    onClick = { viewModel.setPlanAsActive() }
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = AppPadding.MD_PADDING))

                InfoCard(
                    modifier = Modifier.padding(bottom = AppPadding.MD_PADDING),
                    text = stringResource(R.string.plan_address_enabled_change_info)
                )

                OutlinedCheckbox(
                    label = stringResource(R.string.plan_address_enabled),
                    onCheckedChange = { addressEnabled -> viewModel.updateAddressEnabled(addressEnabled) }
                )
            }
        }
    }
}
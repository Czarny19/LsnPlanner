package com.lczarny.lsnplanner.presentation.ui.lessonplan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.InfoCard
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.OutlinedCheckbox
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PredefinedDialogState
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanViewModel

@Composable
fun LessonPlanCreate(navController: NavController, viewModel: LessonPlanViewModel, firstLaunch: Boolean) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    var nameTouched by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            AppNavBar(
                title = stringResource(R.string.route_new_lesson_plan),
                navIcon = {
                    if (firstLaunch.not()) AppBarBackIconButton(onClick = {
                        if (dataChanged) {
                            discardChangesDialogOpen = true
                        } else {
                            navController.popBackStack()
                        }
                    })
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
            InfoCard(
                modifier = Modifier.padding(bottom = AppPadding.MD_PADDING),
                text = stringResource(R.string.plan_form_info)
            )

            OutlinedInputField(
                label = stringResource(R.string.plan_name),
                onValueChange = { name ->
                    nameTouched = true
                    viewModel.updatePlanName(name)
                },
                maxLines = 1,
                maxLength = 32,
                error = if (nameTouched && (lessonPlan?.name?.isEmpty() == true)) InputError.FieldRequired else null
            )

            OutlinedDropDown(
                label = stringResource(R.string.plan_type),
                initialValue = DropDownItem(LessonPlanType.University, LessonPlanType.University.getLabel(context)),
                onValueChange = { planType -> viewModel.updatePlanType(planType.value as LessonPlanType) },
                items = LessonPlanType.entries.map { DropDownItem(it, it.getLabel(context)) }
            )

            OutlinedCheckbox(
                label = stringResource(R.string.plan_address_enabled),
                onCheckedChange = { addressEnabled -> viewModel.updateAddressEnabled(addressEnabled) }
            )

            Spacer(Modifier.weight(1.0f))

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppPadding.MD_PADDING),
                enabled = saveEnabled,
                text = stringResource(R.string.plan_save),
                onClick = { viewModel.savePlan() }
            )
        }
    }
}
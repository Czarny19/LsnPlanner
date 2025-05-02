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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.LessonPlanType
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.InfoCard
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.OutlinedCheckbox
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.ui.lessonplan.LessonPlanViewModel
import com.lczarny.lsnplanner.utils.navigateBackWithDataCheck

@Composable
fun LessonPlanCreate(navController: NavController, viewModel: LessonPlanViewModel, firstPlan: Boolean) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    var nameTouched by remember { mutableStateOf(false) }

    var discardChangesDialogOpen = remember { mutableStateOf(false) }

    DiscardChangesDialog(discardChangesDialogOpen, navController)

    Scaffold(
        topBar = {
            AppNavBar(
                title = stringResource(R.string.route_new_lesson_plan),
                navIconVisible = firstPlan.not(),
                onNavIconClick = { navController.navigateBackWithDataCheck(dataChanged, discardChangesDialogOpen) },
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
            InfoCard(text = stringResource(R.string.plan_form_info))

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
                    .padding(bottom = AppPadding.MD_PADDING)
                    .fillMaxWidth(),
                enabled = saveEnabled,
                text = stringResource(R.string.plan_save),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.savePlan()
                }
            )
        }
    }
}
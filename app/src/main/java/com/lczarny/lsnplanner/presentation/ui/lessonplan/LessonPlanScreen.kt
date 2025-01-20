package com.lczarny.lsnplanner.presentation.ui.lessonplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.InfoField
import com.lczarny.lsnplanner.presentation.components.OutlinedLabeledCheckbox
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.SavingDialog
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.LessonPlanState
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.toLessonPlanTypeLabelMap

@Composable
fun LessonPlanScreen(
    navController: NavController,
    firstLaunch: Boolean,
    lessonPlanId: Long?,
    viewModel: LessonPlanViewModel = hiltViewModel()
) {
    viewModel.initializePlan(firstLaunch, lessonPlanId)

    val screenState by viewModel.screenState.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        LessonPlanState.Loading -> FullScreenLoading()
                        LessonPlanState.Edit -> LessonPlanForm(false, viewModel)
                        LessonPlanState.Saving -> LessonPlanForm(true, viewModel)
                        LessonPlanState.Finished -> {
                            navController.navigate(HomeRoute(firstLaunch = true)) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun LessonPlanForm(saving: Boolean, viewModel: LessonPlanViewModel) {
    val lessonPlanData by viewModel.lessonPlanData.collectAsState()
    val planIsDefaultEnabled by viewModel.planIsDefaultEnabled.collectAsState()
    val planNameError by viewModel.planNameError.collectAsState()

    val toLessonPlanTypeLabelMap = toLessonPlanTypeLabelMap(LocalContext.current)

    lessonPlanData?.let { lessonPlanData ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (lessonPlanData.id != null) R.string.route_edit_lesson_plan else R.string.route_new_lesson_plan)
                )
            },
            bottomBar = {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppPadding.screenPadding),
                    text = stringResource(R.string.plan_save),
                    onClick = { viewModel.savePlan() }
                )
            }
        ) { padding ->
            SavingDialog(saving)
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(AppPadding.screenPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                InfoField(
                    modifier = Modifier.padding(bottom = AppPadding.inputBottomPadding),
                    text = stringResource(R.string.plan_form_info)
                )
                OutlinedInputField(
                    label = stringResource(R.string.plan_name),
                    value = lessonPlanData.name,
                    onValueChange = { name -> viewModel.updatePlanName(name) },
                    maxLines = 1,
                    maxLength = 32,
                    isError = planNameError,
                    errorMsg = stringResource(R.string.field_required)
                )
                OutlinedDropDown(
                    label = stringResource(R.string.plan_type),
                    value = DropDownItem(lessonPlanData.type, toLessonPlanTypeLabelMap.getValue(lessonPlanData.type)),
                    onValueChange = { planType -> viewModel.updatePlanType(planType.value as LessonPlanType) },
                    items = LessonPlanType.entries.map { DropDownItem(it, toLessonPlanTypeLabelMap.getValue(it)) }
                )
                OutlinedLabeledCheckbox(
                    label = stringResource(R.string.plan_make_default),
                    checked = lessonPlanData.isDefault,
                    onCheckedChange = { checked -> viewModel.updatePlanIsDefault(checked) },
                    enabled = planIsDefaultEnabled
                )
            }
        }
    }
}
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.GradingSystem
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.BottomBarButton
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.InfoField
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PredefinedDialogState
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.getDescription
import com.lczarny.lsnplanner.presentation.ui.lessonplan.model.getLabel

@Composable
fun LessonPlanScreen(
    navController: NavController,
    firstLaunch: Boolean,
    lessonPlanId: Long?,
    viewModel: LessonPlanViewModel = hiltViewModel()
) {
    viewModel.initializePlan(lessonPlanId)

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                    when (screenState) {
                        LessonPlanScreenState.Loading -> FullScreenLoading()
                        LessonPlanScreenState.Edit -> LessonPlanForm(navController, viewModel)
                        LessonPlanScreenState.Saving -> FullScreenLoading(stringResource(R.string.saving))
                        LessonPlanScreenState.Finished -> {
                            if (firstLaunch) {
                                navController.navigate(HomeRoute(true)) {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun LessonPlanForm(navController: NavController, viewModel: LessonPlanViewModel) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val isNewPlan by viewModel.isNewPlan.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()
    val formTouched by viewModel.formTouched.collectAsStateWithLifecycle()

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

    lessonPlan?.let { data ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (isNewPlan) R.string.route_new_lesson_plan else R.string.route_edit_lesson_plan),
                    navIcon = {
                        if (isNewPlan.not()) {
                            AppBarBackIconButton(onClick = {
                                if (formTouched) {
                                    discardChangesDialogOpen = true
                                } else {
                                    navController.popBackStack()
                                }
                            })
                        }
                    },
                )
            },
            bottomBar = {
                BottomBarButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.plan_save),
                    enabled = saveEnabled,
                    onClick = { viewModel.savePlan() }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(AppPadding.SCREEN_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                if (isNewPlan) {
                    InfoField(
                        modifier = Modifier.padding(bottom = AppPadding.INPUT_BOTTOM_PADDING),
                        text = stringResource(R.string.plan_form_info)
                    )
                }
                OutlinedInputField(
                    label = stringResource(R.string.plan_name),
                    value = data.name,
                    onValueChange = { name -> viewModel.updatePlanName(name) },
                    maxLines = 1,
                    maxLength = 32,
                )
                OutlinedDropDown(
                    label = stringResource(R.string.plan_type),
                    readOnly = isNewPlan.not(),
                    value = DropDownItem(data.type, data.type.getLabel(context)),
                    onValueChange = { planType -> viewModel.updatePlanType(planType.value as LessonPlanType) },
                    items = LessonPlanType.entries.map { DropDownItem(it, it.getLabel(context)) }
                )
                OutlinedDropDown(
                    label = stringResource(R.string.plan_grading_system),
                    readOnly = isNewPlan.not(),
                    value = DropDownItem(data.gradingSystem, data.gradingSystem.getLabel(context)),
                    onValueChange = { gradingSystem -> viewModel.updateGradingSystem(gradingSystem.value as GradingSystem) },
                    items = GradingSystem.entries.map { DropDownItem(it, it.getLabel(context)) }
                )
                InfoField(
                    iconVisible = false,
                    text = "${stringResource(R.string.plan_grading_system_selected)}\n${data.gradingSystem.getDescription(context)}"
                )
            }
        }
    }
}
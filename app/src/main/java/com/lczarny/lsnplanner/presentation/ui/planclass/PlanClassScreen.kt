package com.lczarny.lsnplanner.presentation.ui.planclass

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
import com.lczarny.lsnplanner.data.local.model.PlanClassType
import com.lczarny.lsnplanner.data.local.model.planClassTypes
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FormSectionHeader
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.OutlinedDateTimePicker
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.OutlinedLabeledCheckbox
import com.lczarny.lsnplanner.presentation.components.OutlinedNumberInputField
import com.lczarny.lsnplanner.presentation.components.OutlinedTimePicker
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.components.SavingDialog
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.planclass.model.PlanClassState
import com.lczarny.lsnplanner.presentation.ui.planclass.model.planClassTypeLabelMap
import com.lczarny.lsnplanner.utils.toDayOfWeekString
import kotlinx.datetime.DayOfWeek

@Composable
fun PlanClassScreen(
    navController: NavController,
    lessonPlanId: Long,
    lessonPlanType: LessonPlanType,
    defaultWeekDay: Int,
    classId: Long? = null,
    viewModel: PlanClassViewModel = hiltViewModel(),
) {
    viewModel.initializePlanClass(lessonPlanId, lessonPlanType, defaultWeekDay, classId)

    val screenState by viewModel.screenState.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        PlanClassState.Loading -> FullScreenLoading()
                        PlanClassState.Edit -> PlanClassForm(false, viewModel)
                        PlanClassState.Saving -> PlanClassForm(true, viewModel)
                        PlanClassState.Finished -> navController.popBackStack()
                    }
                }
            )
        }
    )
}

@Composable
fun PlanClassForm(saving: Boolean, viewModel: PlanClassViewModel) {
    val context = LocalContext.current

    val planClassData by viewModel.planClassData.collectAsState()
    val isEdit by viewModel.isEdit.collectAsState()
    val isCyclical by viewModel.isCyclical.collectAsState()
    val lessonPlanType by viewModel.lessonPlanType.collectAsState()

    val planNameError by viewModel.planNameError.collectAsState()
    val planStartDateError by viewModel.planStartDateError.collectAsState()
    val planClassroomError by viewModel.planClassroomError.collectAsState()

    val planClassTypeLabelMap = lessonPlanType.planClassTypeLabelMap(context)

    planClassData?.let { data ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (data.id != null) R.string.route_edit_plan_class else R.string.route_new_plan_class)
                )
            },
            bottomBar = {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppPadding.screenPadding),
                    text = stringResource(R.string.class_save),
                    onClick = { viewModel.savePlanClass() }
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
                FormSectionHeader(
                    modifier = Modifier.padding(bottom = AppPadding.smPadding),
                    label = stringResource(R.string.class_basic_info)
                )
                OutlinedInputField(
                    label = stringResource(R.string.class_name),
                    value = data.name,
                    onValueChange = { name -> viewModel.updateName(name) },
                    maxLines = 2,
                    maxLength = 100,
                    readOnly = isEdit,
                    enabled = isEdit.not(),
                    isError = planNameError,
                    errorMsg = stringResource(R.string.field_required)
                )
                OutlinedDropDown(
                    label = stringResource(R.string.class_type),
                    value = DropDownItem(data.type, planClassTypeLabelMap.getValue(data.type)),
                    onValueChange = { type -> viewModel.updateClassType(type.value as PlanClassType) },
                    items = lessonPlanType.planClassTypes().map { DropDownItem(it, planClassTypeLabelMap.getValue(it)) }
                )
                OutlinedInputField(
                    label = stringResource(R.string.class_classroom),
                    value = data.classroom ?: "",
                    onValueChange = { classroom -> viewModel.updateClassroom(classroom) },
                    minLines = 1,
                    maxLines = 1,
                    maxLength = 20,
                    isError = planClassroomError,
                    errorMsg = stringResource(R.string.field_required)
                )
                OutlinedInputField(
                    label = stringResource(R.string.class_note),
                    value = data.note ?: "",
                    onValueChange = { name -> viewModel.updateNote(name) },
                    minLines = 3,
                    maxLines = 5,
                    maxLength = 300,
                )
                FormSectionHeader(
                    modifier = Modifier.padding(bottom = AppPadding.inputBottomPadding),
                    label = stringResource(R.string.class_time_info)
                )
                OutlinedLabeledCheckbox(
                    label = stringResource(R.string.class_is_cyclical),
                    checked = isCyclical,
                    onCheckedChange = { checked -> viewModel.updateIsCyclical(checked) },
                )
                if (isCyclical) {
                    OutlinedDropDown(
                        label = stringResource(R.string.class_week_day),
                        value = DropDownItem(data.weekDay ?: 1, (data.weekDay ?: 1).toDayOfWeekString(context)),
                        onValueChange = { weekDay -> viewModel.updateWeekDay(weekDay.value.toString().toInt()) },
                        items = DayOfWeek.entries.map { DropDownItem(it.value, it.value.toDayOfWeekString(context)) }
                    )
                    OutlinedTimePicker(
                        label = stringResource(R.string.class_time),
                        initialHours = data.startHour,
                        intitialMinutes = data.startMinute,
                        onTimeSelected = { hour, minute -> viewModel.updateStartTime(hour, minute) }
                    )
                } else {
                    OutlinedDateTimePicker(
                        initialValue = data.startDate,
                        label = stringResource(R.string.class_date),
                        onDateTimeSelected = { dateMilis -> viewModel.updateStartDate(dateMilis) },
                        isError = planStartDateError,
                        errorMsg = stringResource(R.string.field_required)
                    )
                }
                OutlinedNumberInputField(
                    label = stringResource(R.string.class_duration),
                    value = data.durationMinutes,
                    onValueChange = { duration ->
                        try {
                            viewModel.updateDuration(duration.toString().toInt())
                        } catch (_: NumberFormatException) {
                        }
                    },
                    minValue = 1,
                    maxValue = 600,
                )
            }
        }
    }
}
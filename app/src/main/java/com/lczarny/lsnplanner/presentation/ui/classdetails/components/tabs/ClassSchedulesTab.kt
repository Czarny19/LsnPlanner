package com.lczarny.lsnplanner.presentation.ui.classdetails.components.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.presentation.components.AddFirstItemInfo
import com.lczarny.lsnplanner.presentation.components.DeleteIcon
import com.lczarny.lsnplanner.presentation.components.DropDownIcon
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.ErrorIcon
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.FutureSelectableDates
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.MinDateSelectableDates
import com.lczarny.lsnplanner.presentation.components.OptionsMenuIcon
import com.lczarny.lsnplanner.presentation.components.OutlinedDatePicker
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.OutlinedNumberInputField
import com.lczarny.lsnplanner.presentation.components.OutlinedTimePicker
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.model.mapper.timePreview
import com.lczarny.lsnplanner.presentation.model.mapper.toLabel
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsViewModel
import com.lczarny.lsnplanner.utils.currentTimestampWithTime
import com.lczarny.lsnplanner.utils.isDurationOverMidnight
import com.lczarny.lsnplanner.utils.toDayOfWeekString
import kotlinx.datetime.DayOfWeek

@Composable
fun ClassSchedulesTab(viewModel: ClassDetailsViewModel) {
    val schedules by viewModel.schedules.collectAsStateWithLifecycle()

    val expandedItemIdx = remember { mutableIntStateOf(-1) }

    if (schedules.isEmpty()) {
        AddFirstItemInfo(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppPadding.SCREEN_PADDING),
            label = stringResource(R.string.class_time_add_first),
            buttonLabel = stringResource(R.string.class_time_add),
            onClick = { viewModel.addClassSchedule() }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(AppPadding.SCREEN_PADDING),
            verticalArrangement = Arrangement.spacedBy(AppPadding.LIST_ITEM_PADDING),
        ) {
            itemsIndexed(items = schedules) { index, item -> ClassScheduleItem(viewModel, index, item, expandedItemIdx) }
            item { FabListBottomSpacer() }
        }
    }
}

@Composable
private fun ClassScheduleItem(viewModel: ClassDetailsViewModel, idx: Int, classSchedule: ClassScheduleModel, expandedItemIdx: MutableIntState) {
    val isExpanded = idx == expandedItemIdx.intValue

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()

    var expandedSize = when (classSchedule.type) {
        ClassScheduleType.Single, ClassScheduleType.Weekly -> 588.dp
        ClassScheduleType.Period -> 778.dp
    }

    if (lessonPlan?.addressEnabled == true) expandedSize += 30.dp

    var classroomTouched by remember { mutableStateOf(false) }
    var addressTouched by remember { mutableStateOf(false) }

    val hasErrors = viewModel.isClassScheduleNotValid(classSchedule, classroomTouched, addressTouched)

    var containerColor = MaterialTheme.colorScheme.primaryContainer
    var onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer

    if (hasErrors) {
        containerColor = MaterialTheme.colorScheme.error
        onContainerColor = MaterialTheme.colorScheme.onError
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .height(if (isExpanded) expandedSize else 54.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(AppSizes.CARD_BORDER_WIDTH, containerColor)
    ) {
        ClassScheduleInfoBar(viewModel, idx, classSchedule, expandedItemIdx, containerColor, onContainerColor, hasErrors)

        if (isExpanded) Column(modifier = Modifier.padding(AppPadding.MD_PADDING)) {
            ClassScheduleBaseInfoSection(viewModel, classSchedule)
            ClassScheduleTimeSection(classSchedule, viewModel)
        }
    }
}

@Composable
private fun ClassScheduleInfoBar(
    viewModel: ClassDetailsViewModel,
    idx: Int,
    classSchedule: ClassScheduleModel,
    expandedItemIdx: MutableIntState,
    containerColor: Color,
    onContainerColor: Color,
    hasErrors: Boolean
) {
    val context = LocalContext.current

    val isExpanded = idx == expandedItemIdx.intValue

    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = stringResource(R.string.card_animation)
    )

    Box(modifier = Modifier.background(containerColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandedItemIdx.intValue = if (isExpanded) -1 else idx }
                .padding(AppPadding.MD_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (hasErrors) ErrorIcon(
                modifier = Modifier
                    .size(AppSizes.MD_ICON)
                    .padding(end = AppPadding.SM_PADDING),
                color = onContainerColor
            )

            Text(
                text = classSchedule.timePreview(context),
                style = MaterialTheme.typography.titleSmall,
                color = onContainerColor
            )

            Spacer(modifier = Modifier.weight(1.0f))

            ClassScheduleItemMenu(viewModel, classSchedule, onContainerColor)

            DropDownIcon(
                modifier = Modifier
                    .size(AppSizes.MD_ICON)
                    .padding(start = AppPadding.SM_PADDING)
                    .rotate(rotationState),
                color = onContainerColor
            )
        }
    }
}

@Composable
private fun ClassScheduleItemMenu(viewModel: ClassDetailsViewModel, classSchedule: ClassScheduleModel, onContainerColor: Color) {
    var dropDownExpanded by remember { mutableStateOf(false) }

    IconButton(modifier = Modifier.size(AppSizes.MD_ICON), onClick = { dropDownExpanded = true }) {
        OptionsMenuIcon(tint = onContainerColor)

        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = { dropDownExpanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                leadingIcon = { DeleteIcon() },
                onClick = {
                    dropDownExpanded = false
                    viewModel.deleteClassSchedule(classSchedule)
                }
            )
        }
    }
}

@Composable
private fun ClassScheduleBaseInfoSection(viewModel: ClassDetailsViewModel, classSchedule: ClassScheduleModel) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()

    var classroomTouched by remember { mutableStateOf(false) }
    var addressTouched by remember { mutableStateOf(false) }

    val classroomError = classroomTouched && classSchedule.classroom.isEmpty()
    val addressError = addressTouched && (classSchedule.address?.isEmpty() == true)
    val durationError = isDurationOverMidnight(classSchedule.durationMinutes, classSchedule.startHour, classSchedule.startMinute)

    if (lessonPlan?.addressEnabled == true) OutlinedInputField(
        label = stringResource(R.string.class_address),
        initialValue = classSchedule.address ?: "",
        onValueChange = { address ->
            addressTouched = true
            viewModel.updateClassScheduleAddress(classSchedule, address)
        },
        maxLines = 2,
        maxLength = 100,
        error = if (addressError) InputError.FieldRequired else null
    )

    OutlinedInputField(
        label = stringResource(R.string.class_classroom),
        initialValue = classSchedule.classroom,
        onValueChange = { classroom ->
            classroomTouched = true
            viewModel.updateClassScheduleClassroom(classSchedule, classroom)
        },
        maxLines = 1,
        maxLength = 30,
        error = if (classroomError) InputError.FieldRequired else null
    )

    OutlinedTimePicker(
        label = stringResource(R.string.class_time_starts_at),
        value = currentTimestampWithTime(classSchedule.startHour, classSchedule.startMinute),
        onTimeSelected = { hour, minute -> viewModel.updateClassScheduleStartTime(classSchedule, hour, minute) }
    )

    OutlinedNumberInputField(
        label = stringResource(R.string.class_duration),
        value = classSchedule.durationMinutes,
        onValueChange = { duration -> viewModel.updateClassScheduleDuration(classSchedule, duration) },
        minValue = 1,
        maxValue = 600,
        error = if (durationError) InputError.CustomErrorMsg(stringResource(R.string.error_duration_over_midnight)) else null
    )

    OutlinedDropDown(
        label = stringResource(R.string.class_time_type),
        initialValue = classSchedule.type.let { DropDownItem(it, it.toLabel(context)) },
        onValueChange = { type -> viewModel.updateClassScheduleType(classSchedule, type.value as ClassScheduleType) },
        items = ClassScheduleType.entries.map { DropDownItem(it, it.toLabel(context)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassScheduleTimeSection(classTime: ClassScheduleModel, viewModel: ClassDetailsViewModel) {
    val context = LocalContext.current

    when (classTime.type) {
        ClassScheduleType.Weekly -> {
            OutlinedDropDown(
                label = stringResource(R.string.class_time_week_day),
                initialValue = DropDownItem(classTime.weekDay ?: 1, (classTime.weekDay ?: 1).toDayOfWeekString(context)),
                onValueChange = { weekday -> viewModel.updateClassScheduleWeekday(classTime, weekday.value as Int) },
                items = DayOfWeek.entries.map { DropDownItem(it.value, it.value.toDayOfWeekString(context)) }
            )
        }

        ClassScheduleType.Period -> {
            OutlinedDropDown(
                label = stringResource(R.string.class_time_week_day),
                initialValue = DropDownItem(classTime.weekDay ?: 1, (classTime.weekDay ?: 1).toDayOfWeekString(context)),
                onValueChange = { weekday -> viewModel.updateClassScheduleWeekday(classTime, weekday.value as Int) },
                items = DayOfWeek.entries.map { DropDownItem(it.value, it.value.toDayOfWeekString(context)) },
            )

            OutlinedDatePicker(
                selectableDates = FutureSelectableDates,
                label = stringResource(R.string.class_time_start_date),
                value = classTime.startDate,
                onDateSelected = { startDate -> viewModel.updateClassScheduleStartDate(classTime, startDate) },
                error = if (classTime.startDate == null) InputError.FieldRequired else null
            )

            OutlinedDatePicker(
                enabled = classTime.startDate != null,
                selectableDates = MinDateSelectableDates(classTime.startDate ?: 0L),
                label = stringResource(R.string.class_time_end_date),
                value = classTime.endDate,
                onDateSelected = { endDate -> viewModel.updateClassScheduleEndDate(classTime, endDate) },
                error = if (classTime.endDate == null) InputError.FieldRequired else null
            )
        }

        ClassScheduleType.Single -> {
            OutlinedDatePicker(
                label = stringResource(R.string.class_time_date),
                value = classTime.startDate,
                onDateSelected = { startDate -> viewModel.updateClassScheduleStartDate(classTime, startDate) },
                error = if (classTime.startDate == null) InputError.FieldRequired else null
            )
        }
    }
}


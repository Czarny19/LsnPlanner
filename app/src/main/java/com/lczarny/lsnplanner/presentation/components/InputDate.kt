package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate
import com.lczarny.lsnplanner.utils.convertMillisToSystemDateTime
import com.lczarny.lsnplanner.utils.convertMillisToSystemTime
import com.lczarny.lsnplanner.utils.dateTimeFromEpochMilis

enum class DateTimeInputType {
    Time,
    Date,
    DateTime
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    value: Long?,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    error: InputError? = null,
    onTimeSelected: (Int, Int) -> Unit
) {
    InternalDateTimePicker(
        modifier = modifier,
        inputType = DateTimeInputType.Time,
        selectableDates = null,
        label = label,
        value = value,
        error = error,
        readOnly = readOnly,
        enabled = enabled,
        onDateTimeSelected = null,
        onDateSelected = null,
        onTimeSelected = onTimeSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDatePicker(
    modifier: Modifier = Modifier,
    selectableDates: SelectableDates? = null,
    label: String,
    value: Long?,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    error: InputError? = null,
    onDateSelected: (Long) -> Unit
) {
    InternalDateTimePicker(
        modifier = modifier,
        inputType = DateTimeInputType.Date,
        selectableDates = selectableDates,
        label = label,
        value = value,
        error = error,
        readOnly = readOnly,
        enabled = enabled,
        onDateTimeSelected = null,
        onDateSelected = onDateSelected,
        onTimeSelected = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDateTimePicker(
    modifier: Modifier = Modifier,
    selectableDates: SelectableDates? = null,
    label: String,
    value: Long?,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    error: InputError? = null,
    onDateTimeSelected: (Long?) -> Unit
) {
    InternalDateTimePicker(
        modifier = modifier,
        inputType = DateTimeInputType.DateTime,
        selectableDates = selectableDates,
        label = label,
        value = value,
        error = error,
        readOnly = readOnly,
        enabled = enabled,
        onDateTimeSelected = onDateTimeSelected,
        onDateSelected = null,
        onTimeSelected = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternalDateTimePicker(
    modifier: Modifier = Modifier,
    inputType: DateTimeInputType = DateTimeInputType.DateTime,
    selectableDates: SelectableDates? = null,
    label: String,
    value: Long?,
    error: InputError? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onDateTimeSelected: ((Long?) -> Unit)?,
    onDateSelected: ((Long) -> Unit)?,
    onTimeSelected: ((Int, Int) -> Unit)?
) {
    val context = LocalContext.current

    var dateTimeMilis by remember { mutableLongStateOf(0L) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = when (inputType) {
            DateTimeInputType.Time -> value?.convertMillisToSystemTime(context) ?: ""
            DateTimeInputType.Date -> value?.convertMillisToSystemDate(context) ?: ""
            DateTimeInputType.DateTime -> value?.convertMillisToSystemDateTime(context) ?: ""
        },
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text(stringResource(R.string.select_date)) },
        trailingIcon = {
            when (inputType) {
                DateTimeInputType.Time -> SelectTimeIcon()
                DateTimeInputType.Date, DateTimeInputType.DateTime -> SelectDateIcon()
            }
        },
        isError = error != null,
        readOnly = readOnly,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppPadding.INPUT_BOTTOM_PADDING)
            .pointerInput(value) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        if (inputType == DateTimeInputType.Time) {
                            showTimeDialog = true
                        } else {
                            showDateDialog = true
                        }
                    }
                }
            },
        supportingText = { if (error != null) ErrorSupportingText(error) },
    )

    if (showDateDialog) {
        AppDatePickerDialog(
            initialValue = value,
            selectableDates = selectableDates,
            onDismiss = { showDateDialog = false },
            onConfirm = { selectedDateMillis ->
                dateTimeMilis = selectedDateMillis ?: 0
                showDateDialog = false

                if (inputType == DateTimeInputType.DateTime) {
                    showTimeDialog = true
                } else {
                    onDateSelected?.invoke(dateTimeMilis)
                }
            }
        )
    }

    if (showTimeDialog) {
        AppTimePickerDialog(
            initialValue = dateTimeMilis,
            onDismiss = { showTimeDialog = false },
            onConfirm = { selectedTimeMillis ->
                dateTimeMilis = selectedTimeMillis
                showTimeDialog = false

                if (inputType == DateTimeInputType.DateTime) {
                    onDateTimeSelected?.invoke(dateTimeMilis)
                } else {
                    dateTimeFromEpochMilis(dateTimeMilis).let {
                        onTimeSelected?.invoke(it.hour, it.minute)
                    }
                }
            }
        )
    }
}
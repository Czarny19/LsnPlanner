package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.utils.convertMillisToSystemDateTime
import com.lczarny.lsnplanner.utils.formatTime
import java.util.Calendar

@Composable
fun InfoField(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppPadding.screenPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Icon(
                modifier = Modifier
                    .size(AppSizes.lgIcon)
                    .padding(end = AppPadding.screenPadding),
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.information),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun OutlinedInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    hint: String = "",
    isError: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    errorMsg: String? = null,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int
) {
    OutlinedTextField(
        modifier = modifier
            .padding(bottom = AppPadding.inputBottomPadding)
            .fillMaxWidth(),
        value = value,
        onValueChange = { text -> if (text.length <= maxLength) onValueChange.invoke(text) },
        label = { Text(text = label) },
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        readOnly = readOnly,
        enabled = enabled,
        supportingText = {
            when (isError) {
                true -> Text(
                    text = errorMsg ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error
                )

                false -> Text(
                    text = if (hint.isNotEmpty()) {
                        "$hint | ${value.length} / $maxLength"
                    } else {
                        "${value.length} / $maxLength"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        },
    )
}

@Composable
fun OutlinedNumberInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    OutlinedTextField(
        modifier = modifier
            .padding(bottom = AppPadding.inputBottomPadding)
            .fillMaxWidth(),
        value = value.toString(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        onValueChange = { text ->
            if (text.isEmpty()) {
                onValueChange.invoke(0)
            } else if (text.toInt() in minValue..maxValue) {
                onValueChange.invoke(text.toInt())
            }
        },
        label = { Text(text = label) },
        minLines = 1,
        maxLines = 1,
        isError = value < minValue || value > maxValue,
        readOnly = readOnly,
        enabled = enabled,
        supportingText = {
            if (value < minValue || value > maxValue) {
                Text(
                    text = stringResource(R.string.number_not_in_range_error, minValue, maxValue),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
    )
}

@Composable
fun FullScreenTextArea(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxSize(),
        value = value,
        minLines = 20,
        placeholder = { Text(text = placeholder) },
        onValueChange = { name -> onValueChange.invoke(name) },
        shape = CutCornerShape(0),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )
}

@Composable
fun OutlinedTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    initialHours: Int?,
    intitialMinutes: Int?,
    onTimeSelected: (Int, Int) -> Unit
) {
    val context = LocalContext.current

    var showTimeDialog by remember { mutableStateOf(false) }

    val displayTime = formatTime(context, initialHours, intitialMinutes)

    OutlinedTextField(
        value = displayTime,
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppPadding.inputBottomPadding)
            .pointerInput(displayTime) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) showTimeDialog = true
                }
            }
    )

    if (showTimeDialog) {
        AppTimePickerDialog(
            initialValue = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, initialHours ?: 0)
                set(Calendar.MINUTE, intitialMinutes ?: 0)
            }.timeInMillis,
            onDismiss = { showTimeDialog = false },
            onConfirm = { selectedDateTimeMillis ->
                val time = Calendar.getInstance().apply { timeInMillis = selectedDateTimeMillis }
                onTimeSelected(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE))
                showTimeDialog = false
            }
        )
    }
}

@Composable
fun OutlinedDateTimePicker(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: Long?,
    isError: Boolean = false,
    errorMsg: String? = null,
    onDateTimeSelected: (Long?) -> Unit
) {
    val context = LocalContext.current

    var dateTimeMilis by remember { mutableLongStateOf(0L) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = initialValue?.convertMillisToSystemDateTime(context) ?: "",
        onValueChange = { },
        label = { Text(label) },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppPadding.inputBottomPadding)
            .pointerInput(initialValue) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) showDateDialog = true
                }
            },
        supportingText = {
            if (isError) {
                Text(
                    text = errorMsg ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
    )

    if (showDateDialog) {
        AppDatePickerDialog(
            initialValue = initialValue,
            futureDatesOnly = true,
            onDismiss = { showDateDialog = false },
            onConfirm = { selectedDateMillis ->
                dateTimeMilis = selectedDateMillis ?: 0
                showDateDialog = false
                showTimeDialog = true
            }
        )
    }

    if (showTimeDialog) {
        AppTimePickerDialog(
            initialValue = dateTimeMilis,
            onDismiss = { showTimeDialog = false },
            onConfirm = { selectedDateTimeMillis ->
                dateTimeMilis = selectedDateTimeMillis
                onDateTimeSelected(dateTimeMilis)
                showTimeDialog = false
            }
        )
    }
}

data class DropDownItem(
    val value: Any,
    val description: String
)

@Composable
fun OutlinedDropDown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<DropDownItem>,
    value: DropDownItem,
    onValueChange: (DropDownItem) -> Unit
) {
    var fieldSize by remember { mutableStateOf(Size.Zero) }
    var seleced by remember { mutableStateOf(value) }
    var expanded by remember { mutableStateOf(false) }

    val icon = when (expanded) {
        true -> Icons.Filled.KeyboardArrowUp
        false -> Icons.Filled.KeyboardArrowDown
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppPadding.inputBottomPadding)
                .pointerInput(value) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) expanded = true
                    }
                }
                .onGloballyPositioned { coordinates -> fieldSize = coordinates.size.toSize() },
            value = seleced.description,
            onValueChange = { },
            label = { Text(text = label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { expanded = true },
                    imageVector = icon,
                    contentDescription = stringResource(R.string.show_options),
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange.invoke(item)
                        seleced = item
                        expanded = false
                    },
                    text = { Text(text = item.description) }
                )
            }
        }
    }
}

@Composable
fun OutlinedLabeledCheckbox(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppPadding.inputBottomPadding)
            .border(
                width = 1.dp,
                color = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                shape = OutlinedTextFieldDefaults.shape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppPadding.xsmPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
            Text(text = label)
        }
    }
}
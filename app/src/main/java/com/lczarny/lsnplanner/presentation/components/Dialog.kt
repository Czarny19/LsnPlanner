package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import java.util.Calendar
import java.util.Date

@Composable
fun SavingDialog(show: Boolean) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(AppPadding.mdPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(AppSizes.lgIcon)
                            .padding(bottom = AppPadding.mdPadding)
                            .wrapContentSize(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.saving),
                        modifier = Modifier
                            .padding(top = AppPadding.mdPadding)
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(initialValue: Long?, onDismiss: () -> Unit, onConfirm: (Long?) -> Unit) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialValue)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        confirmButton = {
            TextButton(onClick = { onConfirm.invoke(datePickerState.selectedDateMillis) }) { Text(stringResource(R.string.ok)) }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(initialValue: Long?, onDismiss: () -> Unit, onConfirm: (Long) -> Unit) {
    val time = Calendar.getInstance()
    initialValue?.let {
        time.setTime(Date(it))
    }

    val timePickerState = rememberTimePickerState(
        initialHour = time.get(Calendar.HOUR_OF_DAY),
        initialMinute = time.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text(stringResource(R.string.cancel)) } },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(time.apply {
                    this.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    this.set(Calendar.MINUTE, timePickerState.minute)
                }.timeInMillis)
            }) { Text(stringResource(R.string.ok)) }
        },
        text = { TimePicker(state = timePickerState) }
    )
}
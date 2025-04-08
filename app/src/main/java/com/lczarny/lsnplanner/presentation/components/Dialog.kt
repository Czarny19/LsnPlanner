package com.lczarny.lsnplanner.presentation.components

import android.text.format.DateFormat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.utils.currentTimestampWithTime
import com.lczarny.lsnplanner.utils.dateTimeFromEpochMilis
import java.time.LocalDate
import java.time.ZoneOffset

data class BasicDialogState(
    var title: String,
    var text: String,
    var onDismiss: () -> Unit,
    var onConfirm: () -> Unit
)

data class PredefinedDialogState(
    var onDismiss: () -> Unit,
    var onConfirm: () -> Unit
)

@Composable
fun ConfirmationDialog(visible: Boolean, state: BasicDialogState) {
    if (visible.not()) {
        return
    }

    AlertDialog(
        title = { Text(state.title) },
        text = { Text(state.text) },
        onDismissRequest = state.onDismiss,
        confirmButton = { TextButton(onClick = state.onConfirm) { Text(stringResource(R.string.confirm)) } },
        dismissButton = { TextButton(onClick = state.onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
fun DiscardChangesDialog(visible: MutableState<Boolean>, navController: NavController) {
    if (visible.value.not()) {
        return
    }

    val state = PredefinedDialogState(
        onConfirm = {
            visible.value = false
            navController.popBackStack()
        },
        onDismiss = { visible.value = false }
    )

    AlertDialog(
        title = { Text(stringResource(R.string.discard_changes), color = MaterialTheme.colorScheme.error) },
        text = { Text(stringResource(R.string.discard_changes_question)) },
        onDismissRequest = state.onDismiss,
        confirmButton = {
            TextButton(
                onClick = state.onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) { Text(stringResource(R.string.discard)) }
        },
        dismissButton = { TextButton(onClick = state.onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
fun DeleteItemDialog(visible: Boolean, state: BasicDialogState) {
    if (visible.not()) {
        return
    }

    AlertDialog(
        title = { Text(state.title, color = MaterialTheme.colorScheme.error) },
        text = { Text(state.text) },
        onDismissRequest = state.onDismiss,
        confirmButton = {
            TextButton(
                onClick = state.onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) { Text(stringResource(R.string.delete)) }
        },
        dismissButton = { TextButton(onClick = state.onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
object FutureSelectableDates : SelectableDates {
    private val now = LocalDate.now()
    private val dayStart = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MinDateSelectableDates(private val minDateMilis: Long) : SelectableDates {
    private val now = LocalDate.now()

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= minDateMilis
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(initialValue: Long?, selectableDates: SelectableDates? = null, onDismiss: () -> Unit, onConfirm: (Long?) -> Unit) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialValue,
        selectableDates = selectableDates ?: DatePickerDefaults.AllDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
        confirmButton = {
            TextButton(
                onClick = { onConfirm.invoke(datePickerState.selectedDateMillis) },
                enabled = datePickerState.selectedDateMillis != null
            ) { Text(stringResource(R.string.ok)) }
        }
    ) { DatePicker(state = datePickerState) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(initialValue: Long?, onDismiss: () -> Unit, onConfirm: (Long) -> Unit) {
    val time = dateTimeFromEpochMilis(initialValue ?: 0)

    val timePickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
        is24Hour = DateFormat.is24HourFormat(LocalContext.current),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text(stringResource(R.string.cancel)) } },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentTimestampWithTime(timePickerState.hour, timePickerState.minute)) }) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { TimePicker(state = timePickerState) }
    )
}
package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class InputError {
    data object FieldRequired : InputError()
    data class NumValueOutOfRange(val minValue: Int, val maxValue: Int) : InputError()
    data class CustomErrorMsg(val msg: String) : InputError()
}

@Composable
fun OutlinedInputField(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: String = "",
    hint: String = "",
    error: InputError? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int
) {
    val composableScope = rememberCoroutineScope()
    var debounceJob: Job? by remember { mutableStateOf(null) }
    var fieldValue by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        modifier = modifier
            .padding(bottom = AppPadding.INPUT_BOTTOM_PADDING)
            .fillMaxWidth(),
        value = fieldValue,
        onValueChange = { text ->
            if (text.length <= maxLength) {
                fieldValue = text
                debounceJob?.cancel()
                debounceJob = composableScope.launch {
                    delay(200)
                    onValueChange.invoke(text)
                }
            }
        },
        label = { Text(label) },
        minLines = minLines,
        maxLines = maxLines,
        isError = error != null,
        readOnly = readOnly,
        enabled = enabled,
        supportingText = { if (error != null) ErrorSupportingText(error) else LengthSupportingText(hint, fieldValue, maxLength) }
    )
}

@Composable
fun OutlinedNumberInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    error: InputError? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    val composableScope = rememberCoroutineScope()
    var debounceJob: Job? by remember { mutableStateOf(null) }
    var fieldValue by remember { mutableIntStateOf(value) }

    OutlinedTextField(
        modifier = modifier
            .padding(bottom = AppPadding.INPUT_BOTTOM_PADDING)
            .fillMaxWidth(),
        value = fieldValue.toString(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        onValueChange = { text ->
            try {
                if (text.isEmpty()) {
                    fieldValue = 0
                    debounceJob?.cancel()
                    debounceJob = composableScope.launch {
                        delay(200)
                        onValueChange.invoke(0)
                    }
                } else if (text.toInt() in minValue..maxValue) {
                    fieldValue = text.toInt()
                    debounceJob?.cancel()
                    debounceJob = composableScope.launch {
                        delay(200)
                        onValueChange.invoke(text.toInt())
                    }
                }
            } catch (_: NumberFormatException) {
            }
        },
        label = { Text(label) },
        minLines = 1,
        maxLines = 1,
        isError = error != null || (fieldValue < minValue || fieldValue > maxValue),
        readOnly = readOnly,
        enabled = enabled,
        supportingText = {
            if (error != null) ErrorSupportingText(error)
            else if (fieldValue < minValue || fieldValue > maxValue) ErrorSupportingText(InputError.NumValueOutOfRange(minValue, maxValue))
        }
    )
}

@Composable
fun FullScreenTextArea(placeholder: String, initialValue: String, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        modifier = Modifier.fillMaxSize(),
        value = value,
        minLines = 20,
        placeholder = { Text(placeholder) },
        onValueChange = { text ->
            onValueChange.invoke(text)
            value = text
        },
        shape = CutCornerShape(0),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )
}
package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.lczarny.lsnplanner.R
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
@Stable
inline fun OutlinedInputField(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: String = "",
    hint: String = "",
    error: InputError? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    obscured: Boolean = false,
    crossinline onValueChange: (String) -> Unit,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int? = null,
    noinline supportingText: @Composable (() -> Unit)? = null
) {
    var fieldValue by remember { mutableStateOf(initialValue) }
    var obscureEnabled by remember { mutableStateOf(obscured) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (obscureEnabled) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (obscured) KeyboardType.Password else KeyboardType.Text),
        value = fieldValue,
        onValueChange = { text ->
            if (maxLength == null || text.length <= maxLength) {
                fieldValue = text
                onValueChange.invoke(text)
            }
        },
        label = { Text(label) },
        minLines = minLines,
        maxLines = maxLines,
        isError = error != null,
        readOnly = readOnly,
        enabled = enabled,
        suffix = {
            if (obscured) {
                if (obscureEnabled) {
                    Icon(
                        AppIcons.PASS_HIDDEN,
                        modifier = Modifier.clickable { obscureEnabled = false },
                        contentDescription = stringResource(R.string.signin_show_password),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(
                        AppIcons.PASS_VISIBLE,
                        modifier = Modifier.clickable { obscureEnabled = true },
                        contentDescription = stringResource(R.string.signin_hide_password),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
        supportingText = {
            if (error != null) {
                ErrorSupportingText(error)
            } else if (maxLength != null) {
                LengthSupportingText(hint, fieldValue, maxLength)
            } else supportingText?.invoke()
        }
    )
}

@Composable
@Stable
inline fun OutlinedNumberInputField(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: Int,
    error: InputError? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    crossinline onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    val composableScope = rememberCoroutineScope()
    var debounceJob: Job? by remember { mutableStateOf(null) }
    var fieldValue by remember { mutableIntStateOf(initialValue) }

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
@Stable
inline fun FullScreenTextArea(placeholder: String, initialValue: String, crossinline onValueChange: (String) -> Unit) {
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
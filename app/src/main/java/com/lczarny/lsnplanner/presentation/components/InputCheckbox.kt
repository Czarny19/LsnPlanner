package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lczarny.lsnplanner.presentation.constants.AppPadding

@Composable
inline fun OutlinedCheckbox(
    modifier: Modifier = Modifier,
    label: String,
    initialValue: Boolean = false,
    enabled: Boolean = true,
    crossinline onCheckedChange: (Boolean) -> Unit,
) {
    var fieldValue by remember { mutableStateOf(initialValue) }

    Row(
        modifier = modifier
            .padding(bottom = AppPadding.CHECKBOX_BOTTOM_PADDING)
            .fillMaxWidth()
            .clickable {
                fieldValue = !fieldValue
                onCheckedChange.invoke(fieldValue)
            }
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = OutlinedTextFieldDefaults.shape
            )
            .padding(AppPadding.CHECKBOX_BORDER_PADDING),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = fieldValue,
            enabled = enabled,
            onCheckedChange = { checked ->
                fieldValue = !fieldValue
                onCheckedChange.invoke(checked)
            },
        )
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}
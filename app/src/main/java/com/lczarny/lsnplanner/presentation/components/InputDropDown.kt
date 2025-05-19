package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import com.lczarny.lsnplanner.R

data class DropDownItem(
    val value: Any,
    val description: String,
    val icon: @Composable (() -> Unit)? = null
)

@Composable
inline fun OutlinedDropDown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<DropDownItem>,
    initialValue: DropDownItem,
    readOnly: Boolean = false,
    crossinline onValueChange: (DropDownItem) -> Unit
) {
    var fieldSize by remember { mutableStateOf(Size.Zero) }
    var seleced by remember { mutableStateOf(initialValue) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(seleced) {
                    if (readOnly.not()) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) expanded = true
                        }
                    }
                }
                .onGloballyPositioned { coordinates -> fieldSize = coordinates.size.toSize() },
            value = seleced.description,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            leadingIcon = seleced.icon,
            supportingText = {},
            trailingIcon = {
                if (readOnly.not()) Icon(
                    if (expanded) AppIcons.DROP_DOWN_EXPANDED else AppIcons.DROP_DOWN_NOT_EXPANDED,
                    modifier = Modifier.clickable { expanded = true },
                    contentDescription = stringResource(R.string.show_options)
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
                    text = { Text(item.description) },
                    leadingIcon = item.icon
                )
            }
        }
    }
}
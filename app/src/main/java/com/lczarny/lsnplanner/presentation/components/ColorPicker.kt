package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding

private val colors = listOf<Long>(
    0xffff0000,
    0xffff4000,
    0xffff8000,
    0xff5c7a00,
    0xff278707,
    0xff079191,
    0xff00bfff,
    0xff0080ff,
    0xff0040ff,
    0xff0000ff,
    0xff4000ff,
    0xff8000ff,
    0xffbf00ff,
    0xffff00ff,
    0xffff00bf,
    0xffff0080
)

@Composable
fun ColorPicker(modifier: Modifier = Modifier, label: String, initialColorHex: Long, onColorSelect: (Long) -> Unit) {
    var colorPickerDialogOpen by remember { mutableStateOf(false) }

    ColorPickerDialog(
        visible = colorPickerDialogOpen,
        initialColorHex = initialColorHex,
        onDissmiss = { colorPickerDialogOpen = false },
        onColorSelect = { selectedColorHex ->
            onColorSelect.invoke(selectedColorHex)
            colorPickerDialogOpen = false
        }
    )

    Row(
        modifier = modifier
            .padding(top = AppPadding.CHECKBOX_TOP_PADDING, bottom = AppPadding.CHECKBOX_BOTTOM_PADDING)
            .fillMaxWidth()
            .clickable { colorPickerDialogOpen = true }
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = OutlinedTextFieldDefaults.shape
            )
            .padding(AppPadding.CHECKBOX_BORDER_PADDING),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ColorIndicator(modifier = Modifier.padding(AppPadding.MD_PADDING), color = Color(initialColorHex))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ColorIndicator(modifier: Modifier = Modifier, isLarge: Boolean = false, color: Color, isSelected: Boolean = false) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .size(if (isLarge) 40.dp else 24.dp)
            .background(color)
    ) {
        if (isSelected) Icon(
            Icons.Filled.Check,
            modifier = Modifier.align(Alignment.Center),
            tint = Color.White,
            contentDescription = stringResource(R.string.color_selected)
        )
    }
}

@Composable
fun ColorPickerDialog(visible: Boolean, initialColorHex: Long, onDissmiss: () -> Unit, onColorSelect: (Long) -> Unit) {
    if (visible.not()) {
        return
    }

    var selectedColor by remember { mutableLongStateOf(initialColorHex) }

    AlertDialog(
        title = { Text(stringResource(R.string.color_select)) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(AppPadding.SM_PADDING),
                verticalArrangement = Arrangement.spacedBy(AppPadding.SM_PADDING)
            ) {
                items(items = colors) { color ->
                    ColorIndicator(
                        modifier = Modifier.clickable { selectedColor = color },
                        isLarge = true,
                        color = Color(color),
                        isSelected = selectedColor == color
                    )
                }
            }
        },
        onDismissRequest = onDissmiss,
        confirmButton = { TextButton(onClick = { onColorSelect.invoke(selectedColor) }) { Text(stringResource(R.string.ok)) } },
        dismissButton = { TextButton(onClick = onDissmiss) { Text(stringResource(R.string.cancel)) } }
    )
}
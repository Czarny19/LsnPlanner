package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes

enum class PrimaryButtonVariant {
    Normal,
    Alt,
    Error
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    variant: PrimaryButtonVariant = PrimaryButtonVariant.Normal,
    shape: Shape? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape ?: RoundedCornerShape(AppSizes.ITEM_SHAPE_CORNER_RAD),
        colors = when (variant) {
            PrimaryButtonVariant.Normal -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            PrimaryButtonVariant.Alt -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            PrimaryButtonVariant.Error -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        }
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = AppPadding.XSM_PADDING),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

data class MultiStateSwitchButtonItem<T>(
    val id: T,
    val label: String,
    val imageVector: ImageVector,
    val onClick: () -> Unit
)

// TODO WIP
@Composable
fun <T> MultiStateSwitchButton(modifier: Modifier = Modifier, items: List<MultiStateSwitchButtonItem<T>>, initialStateId: T) {
    var stateId by remember { mutableStateOf(initialStateId) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(AppSizes.ITEM_SHAPE_CORNER_RAD)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.map {
            Button(
                modifier = Modifier.weight(1.0f),
                onClick = {
                    stateId = it.id
                    it.onClick.invoke()
                },
                shape = RoundedCornerShape(AppSizes.ITEM_SHAPE_CORNER_RAD),
                colors = when (it.id == stateId) {
                    true -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    else -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                }
            ) {
                Icon(it.imageVector, contentDescription = it.label)

                Text(
                    it.label,
                    modifier = Modifier.padding(vertical = AppPadding.XSM_PADDING),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
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
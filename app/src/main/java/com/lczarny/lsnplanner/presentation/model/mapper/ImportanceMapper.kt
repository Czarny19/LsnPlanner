package com.lczarny.lsnplanner.presentation.model.mapper

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.Importance

fun Importance.getLabel(context: Context) = when (this) {
    Importance.Normal -> context.getString(R.string.importance_normal)
    Importance.High -> context.getString(R.string.importance_high)
    Importance.VeryHigh -> context.getString(R.string.importance_very_high)
}

fun Importance.getColor() = when (this) {
    Importance.Normal -> Color(0xFF0C5B0F)
    Importance.High -> Color(0xFF9F5F00)
    Importance.VeryHigh -> Color(0xFF7C0B0B)
}

fun Importance.getIcon() = when (this) {
    Importance.Normal -> Icons.Filled.LowPriority
    Importance.High -> Icons.Filled.PriorityHigh
    Importance.VeryHigh -> Icons.Filled.Warning
}
package com.lczarny.lsnplanner.model.mapper

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.database.model.Importance
import com.lczarny.lsnplanner.presentation.components.AppIcons

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
    Importance.Normal -> AppIcons.IMPORTANCE_NORMAL
    Importance.High -> AppIcons.IMPORTANCE_HIGH
    Importance.VeryHigh -> AppIcons.IMPORTANCE_VERY_HIGH
}
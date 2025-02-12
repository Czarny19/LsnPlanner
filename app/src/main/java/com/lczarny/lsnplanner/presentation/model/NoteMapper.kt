package com.lczarny.lsnplanner.presentation.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.NoteImportance

fun NoteImportance.getLabel(context: Context) = when (this) {
    NoteImportance.Low -> context.getString(R.string.note_importance_low)
    NoteImportance.Medium -> context.getString(R.string.note_importance_medium)
    NoteImportance.High -> context.getString(R.string.note_importance_high)
}

fun NoteImportance.getColor() = when (this) {
    NoteImportance.Low -> Color(0xFF0C5B0F)
    NoteImportance.Medium -> Color(0xFF9F5F00)
    NoteImportance.High -> Color(0xFF7C0B0B)
}

fun NoteImportance.getIcon() = when (this) {
    NoteImportance.Low -> Icons.Filled.LowPriority
    NoteImportance.Medium -> Icons.Filled.Schedule
    NoteImportance.High -> Icons.Filled.PriorityHigh
}

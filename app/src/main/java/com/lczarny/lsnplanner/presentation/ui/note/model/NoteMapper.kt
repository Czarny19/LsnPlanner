package com.lczarny.lsnplanner.presentation.ui.note.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.NoteImportance

fun noteImportanceLabelMap(context: Context): Map<NoteImportance, String> = mapOf(
    NoteImportance.Low to context.getString(R.string.note_importance_low),
    NoteImportance.Medium to context.getString(R.string.note_importance_medium),
    NoteImportance.High to context.getString(R.string.note_importance_high)
)

val noteImportanceColorMap: Map<NoteImportance, Color> = mapOf(
    NoteImportance.Low to Color(0xFF0C5B0F),
    NoteImportance.Medium to Color(0xFF9F5F00),
    NoteImportance.High to Color(0xFF7C0B0B)
)

val noteImportanceIconMap: Map<NoteImportance, ImageVector> = mapOf(
    NoteImportance.Low to Icons.Filled.LowPriority,
    NoteImportance.Medium to Icons.Filled.Schedule,
    NoteImportance.High to Icons.Filled.PriorityHigh
)

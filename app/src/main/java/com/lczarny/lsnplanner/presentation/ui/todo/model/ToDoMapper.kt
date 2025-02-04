package com.lczarny.lsnplanner.presentation.ui.todo.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.ToDoImportance

fun toDoImportanceLabelMap(context: Context): Map<ToDoImportance, String> = mapOf(
    ToDoImportance.Low to context.getString(R.string.todo_importance_low),
    ToDoImportance.Medium to context.getString(R.string.todo_importance_medium),
    ToDoImportance.High to context.getString(R.string.todo_importance_high)
)

val toDoImportanceColorMap: Map<ToDoImportance, Color> = mapOf(
    ToDoImportance.Low to Color(0xFF0C5B0F),
    ToDoImportance.Medium to Color(0xFF9F5F00),
    ToDoImportance.High to Color(0xFF7C0B0B)
)

val toDoImportanceIconMap: Map<ToDoImportance, ImageVector> = mapOf(
    ToDoImportance.Low to Icons.Filled.LowPriority,
    ToDoImportance.Medium to Icons.Filled.Schedule,
    ToDoImportance.High to Icons.Filled.PriorityHigh
)

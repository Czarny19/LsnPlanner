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
    Pair(ToDoImportance.Low, context.getString(R.string.todo_importance_low)),
    Pair(ToDoImportance.Medium, context.getString(R.string.todo_importance_medium)),
    Pair(ToDoImportance.High, context.getString(R.string.todo_importance_high))
)

val toDoImportanceColorMap: Map<ToDoImportance, Color> = mapOf(
    Pair(ToDoImportance.Low, Color(0xFF0C5B0F)),
    Pair(ToDoImportance.Medium, Color(0xFF9F5F00)),
    Pair(ToDoImportance.High, Color(0xFF7C0B0B))
)

val toDoImportanceIconMap: Map<ToDoImportance, ImageVector> = mapOf(
    Pair(ToDoImportance.Low, Icons.Filled.LowPriority),
    Pair(ToDoImportance.Medium, Icons.Filled.Schedule),
    Pair(ToDoImportance.High, Icons.Filled.PriorityHigh)
)

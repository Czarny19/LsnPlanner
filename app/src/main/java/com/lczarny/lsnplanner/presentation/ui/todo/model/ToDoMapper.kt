package com.lczarny.lsnplanner.presentation.ui.todo.model

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.ToDoImportance

fun toDoImportanceLabelMap(context: Context): Map<ToDoImportance, String> = mapOf(
    Pair(ToDoImportance.Low, context.getString(R.string.todo_importance_low)),
    Pair(ToDoImportance.Medium, context.getString(R.string.todo_importance_medium)),
    Pair(ToDoImportance.High, context.getString(R.string.todo_importance_high))
)

val toDoImportanceColorMap: Map<ToDoImportance, Color> = mapOf(
    Pair(ToDoImportance.Low, Color.Green),
    Pair(ToDoImportance.Medium, Color.Yellow),
    Pair(ToDoImportance.High, Color.Red)
)

package com.lczarny.lsnplanner.presentation.ui.home.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.presentation.ui.todo.model.toDoImportanceColorMap

@Composable
fun ToDosTab(padding: PaddingValues, viewModel: HomeViewModel = hiltViewModel()) {
    val toDos by viewModel.todos.collectAsState()

    if (toDos.isEmpty()) {
        EmptyList(stringResource(R.string.todo_list_empty_hint))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppPadding.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppPadding.listItemPadding)
        ) {
            itemsIndexed(toDos) { _, toDo ->
                ToDosListItem(toDo)
            }
        }
    }
}

@Composable
fun ToDosListItem(toDo: ToDoModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
        colors = CardDefaults.cardColors(containerColor = toDoImportanceColorMap.getValue(toDo.importance))
    ) {
        Text(text = toDo.content, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium)
        Text(text = toDo.dueDate?.toString() ?: "")
        Text(text = toDo.historical.toString())
    }
}
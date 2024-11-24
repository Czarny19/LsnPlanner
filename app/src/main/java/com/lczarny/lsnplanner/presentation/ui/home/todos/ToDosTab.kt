package com.lczarny.lsnplanner.presentation.ui.home.todos

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.entity.ToDo
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel

@Composable
fun ToDosTab(viewModel: HomeViewModel = hiltViewModel()) {
    val toDos by viewModel.todos.collectAsState()

    if (toDos.isEmpty()) {
        EmptyList(stringResource(R.string.todo_list_empty_hint))
    } else {
        LazyColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            contentPadding = PaddingValues(AppPadding.screenPadding),
        ) {
            itemsIndexed(toDos) { _, toDo ->
                ToDosListItem(toDo)
            }
        }
    }
}

@Composable
fun ToDosListItem(toDo: ToDo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppPadding.mdPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
    ) {
        Text(text = toDo.content)
        Text(text = toDo.dueDate.toString())
    }
}
package com.lczarny.lsnplanner.presentation.ui.home.tab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.ToDoImportance
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.presentation.components.DraggableCard
import com.lczarny.lsnplanner.presentation.components.DraggableCardAction
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.components.TutorialCard
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.navigation.ToDoRoute
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.presentation.ui.todo.model.toDoImportanceColorMap
import com.lczarny.lsnplanner.presentation.ui.todo.model.toDoImportanceIconMap
import com.lczarny.lsnplanner.presentation.ui.todo.model.toDoImportanceLabelMap
import com.lczarny.lsnplanner.utils.convertMillisToSystemDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDosTab(
    padding: PaddingValues,
    viewModel: HomeViewModel,
    navController: NavController,
    lessonPlanId: Long,
) {
    val showHistorical by viewModel.showHistoricalToDos.collectAsState()
    val toDos by viewModel.toDos.collectAsState()
    val todoListSwipeTutorialDone by viewModel.todoListSwipeTutorialDone.collectAsState()

    toDos
        .filter { if (showHistorical) true else !it.historical }
        .sortedBy { it.dueDate }
        .sortedBy { it.importance }
        .sortedBy { it.historical }
        .let { toDos ->
            val toDoImportanceLabelMap = toDoImportanceLabelMap(LocalContext.current)

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
                    if (todoListSwipeTutorialDone.not()) {
                        item {
                            TutorialCard(msg = stringResource(R.string.tutorial_todo_swipe)) {
                                viewModel.markTodoListSwipeTutorialDone()
                            }
                        }
                    }

                    items(items = toDos) { toDo ->
                        ToDosListItem(
                            viewModel,
                            navController,
                            lessonPlanId,
                            toDo,
                            toDoImportanceLabelMap
                        )
                    }
                }
            }
        }
}

@Composable
fun ToDosListItem(
    viewModel: HomeViewModel,
    navController: NavController,
    lessonPlanId: Long,
    toDo: ToDoModel,
    toDoImportanceLabelMap: Map<ToDoImportance, String>
) {
    val contex = LocalContext.current

    DraggableCard(
        clickAction = { navController.navigate(ToDoRoute(lessonPlanId, toDo.id)) },
        endAction = when (toDo.historical) {
            true -> DraggableCardAction(
                color = MaterialTheme.colorScheme.error,
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = stringResource(R.string.delete),
                label = stringResource(R.string.delete),
                onClick = { viewModel.deleteToDo(toDo.id!!) }
            )

            false -> DraggableCardAction(
                color = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(R.string.todo_done),
                label = stringResource(R.string.done),
                onClick = { viewModel.markToDoAsComplete(toDo.id!!) }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(
                    start = AppPadding.screenPadding,
                    end = AppPadding.screenPadding,
                    top = AppPadding.smPadding,
                    bottom = AppPadding.screenPadding
                )
            ) {
                Row(
                    modifier = Modifier.padding(bottom = AppPadding.mdPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AssistChip(
                        modifier = Modifier.height(AppSizes.chipHeight),
                        onClick = { },
                        label = { Text(toDoImportanceLabelMap.getValue(toDo.importance), color = MaterialTheme.colorScheme.onPrimary) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = toDoImportanceColorMap.getValue(toDo.importance),
                            leadingIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        border = null,
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(AppSizes.smIcon),
                                imageVector = toDoImportanceIconMap.getValue(toDo.importance),
                                contentDescription = stringResource(R.string.todo_importance),
                            )
                        }
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    if (toDo.historical) {
                        Icon(
                            modifier = Modifier
                                .size(AppSizes.mdIcon)
                                .padding(end = AppPadding.mdPadding),
                            imageVector = Icons.Outlined.CheckCircleOutline,
                            contentDescription = stringResource(R.string.done),
                        )
                        Text(text = stringResource(R.string.done))
                    } else {
                        toDo.dueDate?.let {
                            Icon(
                                modifier = Modifier
                                    .size(AppSizes.mdIcon)
                                    .padding(end = AppPadding.mdPadding),
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = stringResource(R.string.todo_due_date),
                            )
                            Text(text = it.convertMillisToSystemDateTime(contex))
                        }
                    }
                }
                Text(
                    text = toDo.content,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}